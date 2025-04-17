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
    pass


class DriveFolder(DriveItem):
    def __init__(self, id: str, name: str, mime_type: str):
        super().__init__(id, name, mime_type)
        self.children: list[DriveItem] = []

    def add_child(self, item: DriveItem):
        self.children.append(item)

    def __repr__(self):
        return f"DriveFolder(name='{self.name}', id='{self.id}', children={len(self.children)})"
