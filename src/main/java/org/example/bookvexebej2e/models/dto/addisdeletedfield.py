import re
from pathlib import Path

def add_is_deleted_field(root_dir='.'):
    # Pattern to match files ending with Response.java but not SelectResponse.java
    path_pattern = re.compile(r'.*Response\.java$')
    exclude_pattern = re.compile(r'SelectResponse\.java$')

    # Regex to find the last closing brace of the class file to insert before it
    class_end_pattern = re.compile(r'^\s*}\s*$', re.MULTILINE)

    for java_file in Path(root_dir).rglob('*.java'):
        filename = java_file.name
        if path_pattern.match(filename) and not exclude_pattern.search(filename):
            text = java_file.read_text(encoding='utf-8')

            # Avoid adding duplicate field if already present
            if 'private Boolean isDeleted;' in text:
                print(f"Skipped (already has isDeleted): {java_file}")
                continue
            
            # Find last closing brace to insert before
            matches = list(class_end_pattern.finditer(text))
            if not matches:
                print(f"No class closing brace found in {java_file}")
                continue

            last_brace = matches[-1].start()

            # Determine indentation before the closing brace line
            closing_line = text[last_brace:text.find('\n', last_brace)]
            indent_match = re.match(r'(\s*)}', closing_line)
            indent = indent_match.group(1) if indent_match else '    '

            # Prepare insertion string
            insertion = f"{indent}private Boolean isDeleted;\n"

            # Insert before last closing brace
            new_text = text[:last_brace] + insertion + text[last_brace:]

            java_file.write_text(new_text, encoding='utf-8')
            print(f"Updated {java_file}")

if __name__ == '__main__':
    add_is_deleted_field()
