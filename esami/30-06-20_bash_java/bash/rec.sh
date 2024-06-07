#!/bin/bash

fileToSearch="$1"
dir="$2"
log="$3"

cd "$dir"
for file in *; do

    if [[ -f "$file" && "$file" = "$fileToSearch" ]]; then
        echo "$(date) :: We found $(pwd)/$fileToSearch" >> "$log"
    elif [ -d "$file" ]; then
        echo "invoco ricorsivamente su $(pwd)/$file"
        "$0" "$fileToSearch" "$file" "$log"
    fi
done

