#!/usr/bin/env python3
import re
from pathlib import Path

def transform_dto_file(file_path):
    """Transform DTO fields from UUID references to Response objects"""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # Pattern to match field declarations like: private UUID userId;
    pattern = r'private\s+UUID\s+(\w+)(Id)(?:\s|;)'
    
    # Track transformations for import handling
    transformations = []
    
    def replacement(match):
        field_name = match.group(1)  # e.g., "user" from "userId"
        full_field = match.group(1) + match.group(2)  # e.g., "userId"
        
        # Skip if it's the entity's own ID field (like just "id")
        if field_name.lower() in ['', 'id']:
            return match.group(0)
        
        # Capitalize first letter for the Response class name
        response_class = field_name[0].upper() + field_name[1:] + "Response"
        transformations.append((field_name, response_class))
        
        return f'private {response_class} {field_name};'
    
    # Apply the transformation
    transformed_content = re.sub(pattern, replacement, content)
    
    # Add imports if we found any transformations
    if transformations and transformed_content != original_content:
        # Find the package declaration
        package_match = re.search(r'^package\s+([^;]+);', transformed_content, re.MULTILINE)
        
        if package_match:
            package_end = package_match.end()
            import_section = ""
            
            # Build import statements for each unique response class
            added_imports = set()
            for field_name, response_class in transformations:
                if response_class not in added_imports:
                    # Get the parent directory name (entity name)
                    parent_dir = file_path.parent.name
                    # Build import path based on your structure
                    import_path = f"org.example.bookvexebej2e.models.dto.{parent_dir}.{response_class}"
                    import_section += f"import {import_path};\n"
                    added_imports.add(response_class)
            
            if import_section:
                transformed_content = (transformed_content[:package_end] + 
                                     "\n\n" + import_section + 
                                     transformed_content[package_end:])
    
    # Only write back if changes were made
    if transformed_content != original_content:
        print(f"Transforming: {file_path}")
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(transformed_content)
        return True
    
    return False

def find_and_transform_java_files(start_dir="."):
    """Find and transform Java files in the current directory and one level deep"""
    
    path = Path(start_dir)
    
    # Get all Java files in current directory and immediate subdirectories
    java_files = []
    
    # Files in immediate subdirectories
    for subdir in path.iterdir():
        if subdir.is_dir():
            java_files.extend(subdir.glob("**/*Response.java"))
    
    transformed_count = 0
    
    for java_file in java_files:
        if transform_dto_file(java_file):
            transformed_count += 1
    
    print(f"Transformed {transformed_count} Java files")

if __name__ == "__main__":
    print("Starting DTO transformation...")
    find_and_transform_java_files()
    print("Transformation complete!")
