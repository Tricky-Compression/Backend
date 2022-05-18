#!/usr/bin/env python
N = int(1e2)
with open("large_post_request.json", "w") as f:
    f.write('{\n')
    f.write(f'\t"clientStart": 0,\n')
    f.write(f'\t"clientEnd": 0,\n')
    f.write(f'\t"serverStart": 0,\n')
    f.write(f'\t"serverEnd": 0,\n')
    f.write('\t"filename": "test-large-file",\n')
    data = [50] * N
    f.write(f'\t"data": {data}\n')
    f.write('}\n')

