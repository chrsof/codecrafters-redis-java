#!/bin/bash

function bind_port() {
  printf 'Running test for Stage #JM1 (Bind to a port)\n'
  if ! nc localhost 6379 ; then
    printf 'Connection to port 6379 refused\nTest Failed'
    exit 1
  fi
  printf 'Connection to port 6379 succeeded\nTest Passed\n'
}

function respond_ping() {
  printf 'Running test for Stage #RG2 (Respond to PING)\n'
  response=$(echo "PING\r\n" | nc localhost 6379)
  if [[ "$response" != "+PONG"* ]]; then
    printf 'Expected +PONG received %s\nTest Failed' "$response"
    exit 1
  fi
  printf 'Received +PONG\nTest Passed\n'
}

function test() {
  bind_port
  printf '\n'
  respond_ping
}

if [ $# -eq 0 ]; then
  test
fi

$1