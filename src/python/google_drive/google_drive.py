from datetime import datetime
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow


class Credentials:
    def __init__(self, user_id, access_token=None, refresh_token=None, token_expiry=None):
        self.user_id = user_id
        self.access_token = access_token
        self.refresh_token = refresh_token
        self.token_expiry = token_expiry  # Token expiry time as a timestamp (UNIX time)

    def is_expired(self):
        """Check if the token is expired."""
        if self.token_expiry:
            return datetime.now().timestamp() > self.token_expiry
        return True  # If no expiry is set, consider the token expired

    def refresh(self):
        """Refresh the credentials using the refresh token."""
        creds = Credentials(
            token=self.access_token,
            refresh_token=self.refresh_token,
            token_uri="https://oauth2.googleapis.com/token",
            client_id="your-client-id",  # Set your client ID here
            client_secret="your-client-secret"  # Set your client secret here
        )

        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
            self.access_token = creds.token
            self.token_expiry = creds.expiry.timestamp()
            self.refresh_token = creds.refresh_token  # Update refresh token if changed
        return self

    def to_dict(self):
        """Convert the credentials to a dictionary for easier storage."""
        return {
            "user_id": self.user_id,
            "access_token": self.access_token,
            "refresh_token": self.refresh_token,
            "token_expiry": self.token_expiry
        }

    @classmethod
    def from_dict(cls, data):
        """Create a Credentials instance from a dictionary."""
        return cls(
            user_id=data["user_id"],
            access_token=data["access_token"],
            refresh_token=data["refresh_token"],
            token_expiry=data["token_expiry"]
        )

    @classmethod
    def initialize_credentials(cls, user_id, client_secrets_file, scopes):
        """Initialize credentials when only user_id is known."""
        flow = InstalledAppFlow.from_client_secrets_file(client_secrets_file, scopes)
        creds = flow.run_local_server(port=0)  # This will open a browser for user authentication

        # Create a new credentials object
        credentials = cls(
            user_id=user_id,
            access_token=creds.token,
            refresh_token=creds.refresh_token,
            token_expiry=creds.expiry.timestamp()  # Convert to timestamp
        )

        return credentials


import json
import os

class FileCredentialStore:
    def __init__(self, directory='credentials_store'):
        self.directory = directory
        if not os.path.exists(directory):
            os.makedirs(directory)

    def _get_file_path(self, user_id):
        """Get the path to the user's credential file."""
        return os.path.join(self.directory, f"{user_id}_credentials.json")

    def get_credentials(self, user_id, client_secrets_file, scopes):
        """Retrieve credentials for the given user from the file system."""
        file_path = self._get_file_path(user_id)
        if os.path.exists(file_path):
            with open(file_path, 'r') as file:
                data = json.load(file)
                credentials = Credentials.from_dict(data)
                if not credentials.is_expired():
                    return credentials
                else:
                    # Refresh the token if expired
                    credentials.refresh()
                    self.store_credentials(credentials)
                    return credentials
        else:
            # If no credentials exist, initialize new ones
            return self.initialize_credentials(user_id, client_secrets_file, scopes)

    def store_credentials(self, credentials):
        """Store the credentials for a user in a file."""
        file_path = self._get_file_path(credentials.user_id)
        with open(file_path, 'w') as file:
            json.dump(credentials.to_dict(), file)

    def initialize_credentials(self, user_id, client_secrets_file, scopes):
        """Initialize credentials when only user_id is known."""
        credentials = Credentials.initialize_credentials(user_id, client_secrets_file, scopes)
        self.store_credentials(credentials)
        return credentials

import sqlite3
import os

class SQLiteCredentialStore:
    def __init__(self, db_file='credentials.db'):
        self.db_file = db_file
        self._create_table()

    def _create_table(self):
        """Create the credentials table if it doesn't exist."""
        conn = sqlite3.connect(self.db_file)
        cursor = conn.cursor()
        cursor.execute('''
        CREATE TABLE IF NOT EXISTS credentials (
            user_id TEXT PRIMARY KEY,
            access_token TEXT,
            refresh_token TEXT,
            token_expiry INTEGER
        )
        ''')
        conn.commit()
        conn.close()

    def get_credentials(self, user_id, client_secrets_file, scopes):
        """Retrieve credentials for the given user from the SQLite database."""
        conn = sqlite3.connect(self.db_file)
        cursor = conn.cursor()
        cursor.execute('SELECT access_token, refresh_token, token_expiry FROM credentials WHERE user_id = ?', (user_id,))
        row = cursor.fetchone()
        conn.close()

        if row:
            access_token, refresh_token, token_expiry = row
            credentials = Credentials(user_id, access_token, refresh_token, token_expiry)
            if not credentials.is_expired():
                return credentials
            else:
                # Refresh the token if expired
                credentials.refresh()
                self.store_credentials(credentials)
                return credentials
        else:
            # If no credentials exist, initialize new ones
            return self.initialize_credentials(user_id, client_secrets_file, scopes)

    def store_credentials(self, credentials):
        """Store the credentials for a user in the SQLite database."""
        conn = sqlite3.connect(self.db_file)
        cursor = conn.cursor()
        cursor.execute('''
        INSERT OR REPLACE INTO credentials (user_id, access_token, refresh_token, token_expiry)
        VALUES (?, ?, ?, ?)
        ''', (credentials.user_id, credentials.access_token, credentials.refresh_token, credentials.token_expiry))
        conn.commit()
        conn.close()

    def initialize_credentials(self, user_id, client_secrets_file, scopes):
        """Initialize credentials when only user_id is known."""
        credentials = Credentials.initialize_credentials(user_id, client_secrets_file, scopes)
        self.store_credentials(credentials)
        return credentials
import os
import io
import google.auth
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.http import MediaIoBaseDownload

def list_files(service, folder_id='root'):
    """Recursively lists all files in the specified folder."""
    results = service.files().list(
        q=f"'{folder_id}' in parents", spaces='drive',
        fields="nextPageToken, files(id, name, mimeType)").execute()
    items = results.get('files', [])

    for item in items:
        if item['mimeType'] == 'application/vnd.google-apps.folder':
            print(f"Folder: {item['name']} (ID: {item['id']})")
            list_files(service, item['id'])  # Recursively list subfolders
        else:
            print(f"File: {item['name']} (ID: {item['id']})")


def main():
    creds = authenticate()

    # Build the service object to interact with Google Drive API
    service = build('drive', 'v3', credentials=creds)

    # Starting point (root folder of Google Drive)
    list_files(service)