#!/usr/bin/env bash
curl -H "Content-Type: application/json" --data-binary "@large_post_request.json" http://51.250.23.237:1337/api/upload/single_file
