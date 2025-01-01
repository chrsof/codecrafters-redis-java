#!/bin/bash

function bind_port() {
  printf 'Running test for Stage #JM1 (Bind to a port)\n'
  if ! echo -e 'PING\r\n' | nc -v localhost 6379 -w 1; then
    printf 'Connection to port 6379 refused\nTest Failed'
    exit 1
  fi
  printf 'Connection to port 6379 succeeded\nTest Passed\n'
}

function respond_ping() {
  printf 'Running test for Stage #RG2 (Respond to PING)\n'
  response=$(echo -e "PING\r\n" | nc -v localhost 6379 -w 1)
  if [[ "$response" != "+PONG"* ]]; then
    printf 'Expected +PONG received %s\nTest Failed' "$response"
    exit 1
  fi
  printf 'Received +PONG\nTest Passed\n'
}

function respond_multiple_pings() {
  printf 'Running test for Stage #WY1 (Respond to multiple PINGs)\n'
  response=$(echo -e "PING\r\nPING\r\nPING\r\n" | nc -v localhost 6379 -w 1)
  count=$(echo "$response" | grep -c +PONG)
  if [[ "$count" -ne 3 ]] ; then
    printf 'Expected 3 +PONG replies, got %s\nTest Failed' "$count"
    exit 1
  fi
  printf 'Received %s\nTest Passed\n' "$response"
}

function handle_concurrent_clients() {
  printf 'Running test for Stage #ZU2 (Handle concurrent clients)\n'
  (sleep 3 && respond_ping) &
  (sleep 3 && respond_ping) &
  (sleep 3 && respond_ping) &
  wait
}

function test() {
  bind_port
  printf '\n'
  respond_ping
  printf '\n'
  respond_multiple_pings
  printf '\n'
  handle_concurrent_clients
}

if [ $# -eq 0 ]; then
  test
fi

$1