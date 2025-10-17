import re
from pathlib import Path

def add_post_mappings(root_dir='.'):
    get_pattern = re.compile(
    r'^(\s*)@GetMapping\("/pagination"\)\s*'
    r'public\s+ResponseEntity<Page<([\w]+Response)>>\s+findAll\(\s*(\w+)\s+query\s*\)\s*\{\s*'
    r'return\s+ResponseEntity\.ok\((\w+)\.findAll\(query\)\);\s*\}', re.MULTILINE)
    
    for java_file in Path(root_dir).rglob("*.java"):
        text = java_file.read_text(encoding='utf-8')

        # Find all matches
        matches = list(get_pattern.finditer(text))
        if not matches:
            continue
        
        # Insert PostMapping methods below each found GetMapping method
        inserts = []
        for m in matches:
            indent, response_entity, query_type, service_var = m.groups()
            post_method = (
                f"\n{indent}@PostMapping(\"/pagination\")"
                f"{indent}public ResponseEntity<Page<{response_entity}>> findAll2(@RequestBody {query_type} query) {{"
                f"{indent}    return ResponseEntity.ok({service_var}.findAll(query));"
                f"{indent}}}"
            )
            inserts.append((m.end(), post_method))

        # Insert from bottom to top to not mess up indices
        new_text = text
        offset = 0
        for pos, insertion in sorted(inserts, key=lambda x: x[0]):
            idx = pos + offset
            new_text = new_text[:idx] + insertion + new_text[idx:]
            offset += len(insertion)
        
        java_file.write_text(new_text, encoding='utf-8')
        print(f"✅ Updated: {java_file}")

if __name__ == "__main__":
    add_post_mappings()
