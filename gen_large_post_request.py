#!/usr/bin/env python
N = int(1e7)
with open("large_post_request.json", "w") as f:
    f.write('{\n')
    f.write('\t"filename": "test-large-file",\n')
    data = [48] * N
    f.write(f'\t"data": {data}\n')
    f.write('}\n')

