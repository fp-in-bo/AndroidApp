#!/bin/bash
git log --no-merges --format=%B | head -c 3276  >> CHANGELOG.txt