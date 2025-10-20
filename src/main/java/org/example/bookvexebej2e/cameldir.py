import os
from pathlib import Path
import re

def create_folders_from_first_word():
    current_dir = Path('.')

    for file_path in current_dir.iterdir():
        if file_path.is_file():
            # Extract first CamelCase word from filename stem (without extension)
            # Pattern: match from start uppercase letter followed by lowercase letters
            match = re.match(r'^([A-Z][a-z]+)', file_path.stem)
            if match:
                first_word = match.group(1).lower()
                folder_path = current_dir / first_word
                if not folder_path.exists():
                    folder_path.mkdir()
                    print(f"✅ Created folder: {folder_path}")
                else:
                    print(f"Folder already exists: {folder_path}")

if __name__ == "__main__":
    create_folders_from_first_word()
