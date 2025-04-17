class DriveItem:
    def __init__(self, id: str, name: str, mime_type: str):
        self.id = id
        self.name = name
        self.mime_type = mime_type

    def is_folder(self) -> bool:
        return self.mime_type == "application/vnd.google-apps.folder"

    def __repr__(self):
        return f"{self.__class__.__name__}(name='{self.name}', id='{self.id}')"


class DriveFile(DriveItem):
    pass  # Extend with file-specific behavior later if needed


class DriveFolder(DriveItem):
    pass  # Extend with folder-specific behavior later if needed
