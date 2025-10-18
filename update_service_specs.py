import os
import re
import glob

# Tìm tất cả các Service implementation files
base_path = r"d:\J2EE\bookvexe-be-j2e\src\main\java\org\example\bookvexebej2e\services"
pattern = "**/*ServiceImpl.java"

def update_service_impl(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Tìm và thay thế dòng isDeleted filter trong buildSpecification
        old_pattern = r'predicates\.add\(cb\.or\(cb\.equal\(root\.get\("isDeleted"\), false\), cb\.isNull\(root\.get\("isDeleted"\)\)\)\);'
        replacement = '// Remove the isDeleted filter to show all records including deleted ones'
        
        if re.search(old_pattern, content):
            new_content = re.sub(old_pattern, replacement, content)
            
            # Ghi lại file
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
                
            print(f"Updated: {filepath}")
            return True
        else:
            # Tìm pattern khác có thể có
            old_pattern2 = r'predicates\.add\(cb\.or\(cb\.isFalse\(root\.get\("isDeleted"\)\), cb\.isNull\(root\.get\("isDeleted"\)\)\)\);'
            if re.search(old_pattern2, content):
                new_content = re.sub(old_pattern2, replacement, content)
                
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                    
                print(f"Updated (pattern 2): {filepath}")
                return True
            else:
                print(f"No isDeleted filter found in: {filepath}")
                return False
        
    except Exception as e:
        print(f"Error updating {filepath}: {e}")
        return False

# Tìm và cập nhật tất cả Service implementation files
service_files = []
for root, dirs, files in os.walk(base_path):
    for file in files:
        if file.endswith('ServiceImpl.java'):
            service_files.append(os.path.join(root, file))

print(f"Found {len(service_files)} ServiceImpl files to check")

updated_count = 0
for filepath in service_files:
    if update_service_impl(filepath):
        updated_count += 1

print(f"Updated {updated_count} files")