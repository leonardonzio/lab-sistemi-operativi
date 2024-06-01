#!/bin/bash

str="$1"
ext="$2"
dir="$3"
pid="$4"

cd "$dir"
for file in *; do

    if [[ -d "$file" ]]; then
        echo "invoco ricorsivamente su $(pwd)/$file"
        "$0" "$1" "$2" "$file" $pid
    elif [[ "$file" = *."$ext" ]]; then
        X=$(grep -o -c "$str" "$file")
        echo "Report $pid: il file $(pwd)/$file contiene $X occorrenze di $str" >> "$HOME"/report.log
    fi
done


