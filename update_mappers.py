import os
import re
import glob

# Tìm tất cả các Mapper files
base_path = r"d:\J2EE\bookvexe-be-j2e\src\main\java\org\example\bookvexebej2e\mappers"
pattern = "*Mapper.java"

def update_mapper_class(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Kiểm tra xem đã có @AfterMapping chưa
        if '@AfterMapping' in content:
            print(f"Already updated: {filepath}")
            return False
            
        # Tìm toResponse method
        response_method_match = re.search(r'(\w+Response)\s+toResponse\(\w+\s+(\w+)\);', content)
        if not response_method_match:
            print(f"No toResponse method found in: {filepath}")
            return False
            
        response_class = response_method_match.group(1)
        param_name = response_method_match.group(2)
        
        # Tìm tên entity class từ parameter
        entity_class_match = re.search(r'(\w+DbModel)\s+' + param_name, content)
        if not entity_class_match:
            print(f"Could not find entity class in: {filepath}")
            return False
            
        entity_class = entity_class_match.group(1)
        
        # Thêm import statements
        import_lines = [
            "import org.mapstruct.AfterMapping;",
            "import org.mapstruct.MappingTarget;"
        ]
        
        for import_line in import_lines:
            if import_line not in content:
                # Tìm vị trí để thêm import (sau import Mapper)
                mapper_import_pos = content.find("import org.mapstruct.Mapper;")
                if mapper_import_pos != -1:
                    insert_pos = content.find("\n", mapper_import_pos) + 1
                    content = content[:insert_pos] + import_line + "\n" + content[insert_pos:]
                
        # Tạo @AfterMapping method
        after_mapping_method = f"""
    @AfterMapping
    default void setPermissions(@MappingTarget {response_class} response, {entity_class} entity) {{
        if (response != null && entity != null) {{
            response.setIsDeleted(entity.getIsDeleted());
        }}
    }}"""
        
        # Tìm vị trí cuối interface để thêm method
        class_end_pos = content.rfind('}')
        if class_end_pos != -1:
            content = content[:class_end_pos] + after_mapping_method + '\n}\n'
        
        # Ghi lại file
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
            
        print(f"Updated: {filepath}")
        return True
        
    except Exception as e:
        print(f"Error updating {filepath}: {e}")
        return False

# Tìm và cập nhật tất cả Mapper files
mapper_files = glob.glob(os.path.join(base_path, pattern))

print(f"Found {len(mapper_files)} Mapper files to check")

updated_count = 0
for filepath in mapper_files:
    if update_mapper_class(filepath):
        updated_count += 1

print(f"Updated {updated_count} files")