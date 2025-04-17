from datetime import datetime
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow

import json

class ClientSecrets:
    def __init__(self, client_secrets_path: str = 'client_secrets.json'):
        """
        Initializes the ClientSecrets class and loads the client_id and client_secret
        from the provided client_secrets.json file.
        """
        self.client_secrets_path: str = client_secrets_path
        self.client_id: str
        self.client_secret: str
        self.client_id, self.client_secret = self.load_client_secrets()

    def load_client_secrets(self) -> tuple[str, str]:
        """
        Load client_id and client_secret from the client_secrets.json file.
        """
        with open(self.client_secrets_path, 'r') as file:
            client_secrets = json.load(file)

        client_id: str = client_secrets['installed']['client_id']
        client_secret: str = client_secrets['installed']['client_secret']

        return client_id, client_secret

    def get_client_id(self) -> str:
        return self.client_id

    def get_client_secret(self) -> str:
        return self.client_secret


import os
from google.oauth2.credentials import Credentials
from google.auth.transport.requests import Request


class UserCredentials:
    def __init__(self, client_secrets: ClientSecrets, token_path: str = 'token.json'):
        """
        Initializes the UserCredentials class with the given client_secrets and token path.
        """
        self.client_secrets: ClientSecrets = client_secrets  # Instance of ClientSecrets
        self.token_path: str = token_path
        self.creds: Credentials | None = None  # Credentials object or None

    def build_credentials(self, scopes: list[str] = None) -> Credentials:
        """
        Builds the credentials for the user, either from the token file or by going through OAuth flow.
        """
        if scopes is None:
            scopes = ['https://www.googleapis.com/auth/drive.readonly']

        # Load existing credentials if available
        if os.path.exists(self.token_path):
            self.creds = Credentials.from_authorized_user_file(self.token_path, scopes)

        # If credentials are expired or not present, refresh or start OAuth flow
        if not self.creds or not self.creds.valid:
            if self.creds and self.creds.expired and self.creds.refresh_token:
                self.creds.refresh(Request())
            else:
                flow = InstalledAppFlow.from_client_secrets_file(
                    'client_secrets.json', scopes)
                self.creds = flow.run_local_server(port=0)

            # Save the credentials for the next run
            with open(self.token_path, 'w') as token_file:
                token_file.write(self.creds.to_json())

        return self.creds

    def refresh_credentials(self) -> None:
        """
        Refresh the credentials if they are expired.
        """
        if self.creds and self.creds.expired and self.creds.refresh_token:
            self.creds.refresh(Request())
            # Save the updated credentials
            with open(self.token_path, 'w') as token_file:
                token_file.write(self.creds.to_json())


from googleapiclient.discovery import build
from googleapiclient.discovery import Resource


class GoogleDriveService:
    def __init__(self, user_credentials: UserCredentials):
        """
        Initializes the GoogleDriveService class with user credentials.
        """
        self.user_credentials: UserCredentials = user_credentials
        self.drive_service: Resource | None = None  # API service or None

    def build_service(self) -> Resource:
        """
        Builds the Google Drive API service object using user credentials.
        """
        creds = self.user_credentials.build_credentials()
        self.drive_service = build('drive', 'v3', credentials=creds)
        return self.drive_service

    def list_files(self, page_size: int = 10) -> None:
        """
        List the first 'page_size' files from Google Drive.
        """
        if self.drive_service is None:
            self.drive_service = self.build_service()

        results = self.drive_service.files().list(
            pageSize=page_size, fields="nextPageToken, files(id, name)"
        ).execute()

        items = results.get('files', [])

        if not items:
            print('No files found.')
        else:
            print('Files:')
            for item in items:
                print(f'{item["name"]} ({item["id"]})')


def main() -> None:
    # Instantiate the ClientSecrets class to get client_id and client_secret
    client_secrets = ClientSecrets()

    # Instantiate the UserCredentials class using the client_secrets object
    user_credentials = UserCredentials(client_secrets)

    # Instantiate GoogleDriveService using the user_credentials object
    drive_service = GoogleDriveService(user_credentials)

    # List files on the user's Google Drive
    drive_service.list_files(page_size=5)

if __name__ == '__main__':
    main()
