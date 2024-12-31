#!/bin/bash

function bind_port() {
  printf 'Running test for Stage #JM1 (Bind to a port)\n'
  if ! nc localhost 6379 ; then
    printf 'Connection to port 6379 refused\nTest Failed'
    exit 1
  fi
  printf 'Connection to port 6379\nTest Passed\n'
}

function test() {
  bind_port
}

if [ $# -eq 0 ]; then
  test
fi

$1