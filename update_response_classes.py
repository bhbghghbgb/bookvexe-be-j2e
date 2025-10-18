import os
import re
import glob

# Tìm tất cả các Response files
base_path = r"d:\J2EE\bookvexe-be-j2e\src\main\java\org\example\bookvexebej2e\models\dto"
pattern = "**/*Response.java"

# Loại trừ SelectResponse và base class
exclude_patterns = ["Select", "BasePermissionResponse"]

def should_skip_file(filepath):
    for pattern in exclude_patterns:
        if pattern in filepath:
            return True
    return False

def update_response_class(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Kiểm tra xem đã có isDeleted chưa
        if 'Boolean isDeleted' not in content:
            print(f"Skipping {filepath} - no isDeleted field")
            return False
            
        # Kiểm tra xem đã extend BasePermissionResponse chưa
        if 'extends BasePermissionResponse' in content:
            print(f"Already updated: {filepath}")
            return False
            
        # Tìm package name
        package_match = re.search(r'package ([^;]+);', content)
        if not package_match:
            print(f"Could not find package in {filepath}")
            return False
            
        package_name = package_match.group(1)
        
        # Tìm class name
        class_match = re.search(r'public class (\w+)', content)
        if not class_match:
            print(f"Could not find class name in {filepath}")
            return False
            
        class_name = class_match.group(1)
        
        # Thêm import cho BasePermissionResponse và EqualsAndHashCode
        import_section = f"""import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;"""
        
        # Thay thế @Data
        content = re.sub(r'@Data\npublic class', f'@Data\n@EqualsAndHashCode(callSuper = true)\npublic class', content)
        
        # Thay thế class declaration
        content = re.sub(r'public class (\w+) \{', r'public class \1 extends BasePermissionResponse {', content)
        
        # Thêm constructor và setter method
        constructor_and_setter = f"""    
    public {class_name}() {{
        super();
    }}
    
 
    public void setIsDeleted(Boolean isDeleted) {{
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }}"""
        
        # Tìm vị trí cuối class để thêm methods
        class_end_pos = content.rfind('}')
        if class_end_pos != -1:
            content = content[:class_end_pos] + constructor_and_setter + '\n}\n'
        
        # Thêm import statements
        content = re.sub(
            r'(import lombok\.Data;)',
            import_section,
            content
        )
        
        # Loại bỏ duplicate imports
        lines = content.split('\n')
        seen_imports = set()
        filtered_lines = []
        
        for line in lines:
            if line.strip().startswith('import '):
                if line.strip() not in seen_imports:
                    seen_imports.add(line.strip())
                    filtered_lines.append(line)
            else:
                filtered_lines.append(line)
        
        content = '\n'.join(filtered_lines)
        
        # Ghi lại file
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
            
        print(f"Updated: {filepath}")
        return True
        
    except Exception as e:
        print(f"Error updating {filepath}: {e}")
        return False

# Tìm và cập nhật tất cả Response files
response_files = []
for root, dirs, files in os.walk(base_path):
    for file in files:
        if file.endswith('Response.java') and not should_skip_file(file):
            response_files.append(os.path.join(root, file))

print(f"Found {len(response_files)} Response files to check")

updated_count = 0
for filepath in response_files:
    if update_response_class(filepath):
        updated_count += 1

print(f"Updated {updated_count} files")