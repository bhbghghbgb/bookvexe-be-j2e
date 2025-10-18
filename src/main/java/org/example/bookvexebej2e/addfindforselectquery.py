#!/usr/bin/env python3
import re
from pathlib import Path

def add_find_all_for_select_with_query(file_path):
    """Add findAllForSelect method with query parameter to Service, Controller, and ServiceImpl files"""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # Determine file type and entity name
    file_name = file_path.name
    if file_name.endswith('Service.java'):
        file_type = 'service_interface'
        entity_name = file_name.replace('Service.java', '')
    elif file_name.endswith('ServiceImpl.java'):
        file_type = 'service_impl'
        entity_name = file_name.replace('ServiceImpl.java', '')
    elif file_name.endswith('Controller.java'):
        file_type = 'controller'
        entity_name = file_name.replace('Controller.java', '')
    else:
        return False  # Skip non-target files
    
    # Extract package to determine import path
    package_match = re.search(r'^package\s+([^;]+);', content, re.MULTILINE)
    if not package_match:
        return False
    
    package_path = package_match.group(1)
    
    # Find the correct target method to insert after
    if file_type == 'service_interface':
        # Look for List<EntitySelectResponse> findAllForSelect();
        target_method_pattern = r'List<{}SelectResponse>\s+findAllForSelect\(\s*\);\s*'.format(entity_name)
    elif file_type == 'service_impl':
        # Look for the implementation of findAllForSelect without parameters
        target_method_pattern = r'public\s+List<{}SelectResponse>\s+findAllForSelect\(\s*\)\s*{{[^}}]+}}'.format(entity_name)
    elif file_type == 'controller':
        # Look for the controller method without parameters
        target_method_pattern = r'ResponseEntity<List<{}SelectResponse>>\s+findAllForSelect\(\s*\)\s*{{[^}}]+}}'.format(entity_name)
    
    target_method_match = re.search(target_method_pattern, content, re.DOTALL)
    
    if not target_method_match:
        print(f"Target method (findAllForSelect without params) not found in {file_path}")
        return False
    
    # Generate the new method based on file type
    if file_type == 'service_interface':
        new_method = f"""
    Page<{entity_name}SelectResponse> findAllForSelect({entity_name}Query query);
"""
    elif file_type == 'service_impl':
        # Generate proper field name (camelCase)
        field_name = entity_name[0].lower() + entity_name[1:]
        
        new_method = f"""
    @Override
    public Page<{entity_name}SelectResponse> findAllForSelect({entity_name}Query query) {{
        Specification<{entity_name}DbModel> spec = buildSpecification(query);
        Pageable pageable = buildPageable(query);
        Page<{entity_name}DbModel> entities = {field_name}Repository.findAll(spec, pageable);
        return entities.map({field_name}Mapper::toSelectResponse);
    }}
"""
    elif file_type == 'controller':
        # Generate proper service field name (camelCase)
        service_field_name = entity_name[0].lower() + entity_name[1:] + "Service"
        
        new_method = f"""
    @GetMapping("/select/pagination")
    public ResponseEntity<Page<{entity_name}SelectResponse>> findAllForSelect({entity_name}Query query) {{
        return ResponseEntity.ok({service_field_name}.findAllForSelect(query));
    }}

    @PostMapping("/select/pagination")
    public ResponseEntity<Page<{entity_name}SelectResponse>> findAllForSelect2(@RequestBody {entity_name}Query query) {{
        return ResponseEntity.ok({service_field_name}.findAllForSelect(query));
    }}
"""
    
    # Insert the new method after the target method
    insertion_point = target_method_match.end()
    
    # Check if the method already exists
    existing_method_pattern = r'findAllForSelect\(\s*{}\s*query\s*\)'.format(entity_name)
    if re.search(existing_method_pattern, content, re.IGNORECASE):
        print(f"Method already exists in {file_path}")
        return False
    
    # Insert the new method
    transformed_content = content[:insertion_point] + new_method + content[insertion_point:]
    
    # For ServiceImpl, check if mapper field exists and add it if missing
    if file_type == 'service_impl':
        # Generate proper field names
        field_name = entity_name[0].lower() + entity_name[1:]
        mapper_field_name = f"private final {entity_name}Mapper {field_name}Mapper;"
        
        # Check if mapper field exists
        if f"{field_name}Mapper" not in content:
            # Find the repository field to insert after
            repo_field_pattern = r'private\s+final\s+{}Repository\s+{}Repository;\s*'.format(entity_name, field_name)
            repo_field_match = re.search(repo_field_pattern, content)
            
            if repo_field_match:
                # Insert mapper field after repository field
                repo_field_end = repo_field_match.end()
                mapper_field = f"\n    private final {entity_name}Mapper {field_name}Mapper;"
                transformed_content = transformed_content[:repo_field_end] + mapper_field + transformed_content[repo_field_end:]
                
                # Add mapper import
                mapper_import = f"import {package_path}.{entity_name}Mapper;"
                if f"import {package_path}.{entity_name}Mapper;" not in transformed_content:
                    package_end = package_match.end()
                    transformed_content = transformed_content[:package_end] + '\n' + mapper_import + transformed_content[package_end:]
    
    # Add necessary imports
    imports_to_add = []
    
    if file_type == 'service_interface' or file_type == 'service_impl':
        if f'Page<{entity_name}SelectResponse>' in new_method and f'import org.springframework.data.domain.Page;' not in content:
            imports_to_add.append('import org.springframework.data.domain.Page;')
        
        if file_type == 'service_impl':
            if f'Specification<{entity_name}DbModel>' in new_method and f'import org.springframework.data.jpa.domain.Specification;' not in content:
                imports_to_add.append('import org.springframework.data.jpa.domain.Specification;')
            if 'Pageable pageable' in new_method and f'import org.springframework.data.domain.Pageable;' not in content:
                imports_to_add.append('import org.springframework.data.domain.Pageable;')
    
    elif file_type == 'controller':
        if '@GetMapping' in new_method and f'import org.springframework.web.bind.annotation.GetMapping;' not in content:
            imports_to_add.append('import org.springframework.web.bind.annotation.GetMapping;')
        if '@PostMapping' in new_method and f'import org.springframework.web.bind.annotation.PostMapping;' not in content:
            imports_to_add.append('import org.springframework.web.bind.annotation.PostMapping;')
        if '@RequestBody' in new_method and f'import org.springframework.web.bind.annotation.RequestBody;' not in content:
            imports_to_add.append('import org.springframework.web.bind.annotation.RequestBody;')
    
    # Add imports after package declaration
    if imports_to_add:
        package_end = package_match.end()
        import_section = '\n'.join(imports_to_add) + '\n'
        transformed_content = transformed_content[:package_end] + '\n' + import_section + transformed_content[package_end:]
    
    # Only write back if changes were made
    if transformed_content != original_content:
        print(f"Updating: {file_path}")
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(transformed_content)
        return True
    
    return False

def find_and_update_java_files(start_dir="."):
    """Find and update Service, Controller, and ServiceImpl files recursively"""
    
    java_files = list(Path(start_dir).rglob("*.java"))
    updated_count = 0
    
    target_files = []
    for java_file in java_files:
        filename = java_file.name
        if filename.endswith(('Service.java', 'ServiceImpl.java', 'Controller.java')):
            target_files.append(java_file)
    
    print(f"Found {len(target_files)} target files")
    
    for java_file in target_files:
        if add_find_all_for_select_with_query(java_file):
            updated_count += 1
    
    print(f"Updated {updated_count} Java files")

if __name__ == "__main__":
    print("Starting method addition...")
    find_and_update_java_files()
    print("Update complete!")
