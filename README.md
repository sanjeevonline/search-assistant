# Search Assistant

A full-stack RAG (Retrieval-Augmented Generation) prototype featuring a Java backend and a React frontend. The system ingests mock medical plan documents and allows users to query coverage details via a chat interface.

## Prerequisites

Before running the project, ensure you have the following installed:

-   **Java JDK 11+** (Verified with JDK 17)
-   **Maven** 3.x+
-   **Node.js** 18+
-   **npm** 9+

## Project Structure

-   `backend/src/main/java`: Backend source code (Javalin server, Knowledge Pipeline).
-   `backend/src/test/java`: JUnit 5 test suite.
-   `backend/pom.xml`: Maven dependency configuration.
-   `frontend/`: React + Vite frontend application.

## Getting Started

### 1. Start the Backend API

The backend runs on port `7070`.

```bash
# In the root directory (search-assistant/)
./run.sh

# Or to run only the backend
cd backend
mvn clean compile exec:java -Dexec.mainClass="com.searchassistant.Main"

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
cd backend
mvn test
```

## Debugging (VS Code)

I have configured the project for integrated debugging in VS Code.

1.  **Open the "Run and Debug" side bar** (Ctrl+Shift+D or Cmd+Shift+D).
2.  Select **"Debug Full Stack"** from the dropdown menu.
3.  Press **F5** or the green "Play" button.

This will:
- Start the Java Backend with a debugger attached.
- Launch a Chrome window for the Frontend with a debugger attached.

> [!NOTE]
> Make sure the frontend dev server is running (`cd frontend && npm run dev`) before starting the frontend debugger.

## Features

-   **Hybrid Search**: Combines keyword filtering and simulated vector retrieval.
-   **Post-Processing**: Implements PHI redaction, tone checks, and automated disclaimers.
-   **Demo Data**: Includes mock data for "Plan X" and "Plan Y" covering MRI, CT Scans, and Pharmacy.

## Production Improvements (Mocked Elements)

For this prototype, several core RAG components are simplified or mocked with placeholders. In a production environment, these would be substituted with scalable services:

-   **Distributed Indexing**: A distributed Inverted Index (e.g., ElasticSearch) or Key-Value store (e.g., Redis) to handle millions of terms instead of in-memory maps.
-   **Asynchronous Ingestion**: A document data ingestion process using a message queue (e.g., Kafka) to handle large batch processing.
-   **Embedding Models**: High-performance embedding models (e.g., OpenAI text-embedding-3, Cohere) for semantic extraction.
-   **Advanced Chunking**: Sophisticated strategies like `RecursiveCharacterTextSplitter` or semantic chunking.
-   **Vector Databases**: Dedicated vector databases (e.g., Pinecone, Weaviate) for efficient high-dimensional similarity search.
-   **Production LLMs**: Integration with production-grade LLMs (e.g., GPT-4, Gemini 1.5 Pro) for answer generation.
