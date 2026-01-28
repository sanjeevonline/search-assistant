# Search Assistant

A full-stack RAG (Retrieval-Augmented Generation) prototype featuring a Java backend and a React frontend. The system ingests mock medical plan documents and allows users to query coverage details via a chat interface.

## Prerequisites

Before running the project, ensure you have the following installed:

-   **Java JDK 11+** (Verified with JDK 17)
-   **Maven** 3.x+
-   **Node.js** 18+
-   **npm** 9+

## Project Structure

-   `src/main/java`: Backend source code (Javalin server, Knowledge Pipeline).
-   `src/test/java`: JUnit 5 test suite.
-   `frontend/`: React + Vite frontend application.
-   `pom.xml`: Maven dependency configuration.

## Getting Started

### 1. Start the Backend API

The backend runs on port `7070`.

```bash
# In the root directory (search-assistant/)
./run.sh
```

*You should see "Search Assistant API running on port 7070..."*

### 2. Start the Frontend

The frontend runs on port `5173` (default).

```bash
# Open a new terminal tab/window
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

### 3. Usage

1.  Open your browser to the URL displayed in the frontend terminal (usually `http://localhost:5173`).
2.  Use the **Suggested Prompts** (e.g., "Is MRI covered for Plan X?") or type your own query.
3.  The backend will retrieve relevant documents, simulated an LLM response, and return the answer.

## Running Tests

To run the backend unit tests (including Pipeline logic and PostChecks):

```bash
mvn test
```

## Features

-   **Hybrid Search**: Combines keyword filtering and simulated vector retrieval.
-   **Post-Processing**: Implements PHI redaction, tone checks, and automated disclaimers.
-   **Demo Data**: Includes mock data for "Plan X" and "Plan Y" covering MRI, CT Scans, and Pharmacy.
