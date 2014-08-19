#!/usr/bin/env bash

# Limit memory usage: this application needs very little.
# In this case it uses only about 2% as with a default call.
# Other cases will save a lot less, but the saving will still be significant.
# Use it as a template for your other Java applications to curb memory usage.

# Make sure you are in the right directory.
# Only start the Java application when moving to the right directory was succesful.
cd "${0%/*}" && java -Xms2M -Xmx3M ShowMemory &
