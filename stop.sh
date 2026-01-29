#!/bin/bash

echo "Stopping Search Assistant Services..."

# Define ports
BACKEND_PORT=7070
FRONTEND_PORT=5173

# Function to kill process on a port
kill_port() {
    local port=$1
    local name=$2
    local pid=$(lsof -t -i:$port)

    if [ -n "$pid" ]; then
        echo "Stopping $name (PID: $pid) on port $port..."
        kill -9 $pid
        echo "$name stopped."
    else
        echo "$name is not running on port $port."
    fi
}

kill_port $BACKEND_PORT "Backend"
kill_port $FRONTEND_PORT "Frontend"

echo "Shutdown complete."
