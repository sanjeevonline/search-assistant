import React, { useState } from 'react';
import ChatBox from './components/ChatBox';
import PromptSuggestions from './components/PromptSuggestions';

function App() {
  const [messages, setMessages] = useState([
    { role: 'assistant', content: 'Hello! I am your benefits search assistant. Ask me about your plan coverage.' }
  ]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const sendMessage = async (text) => {
    if (!text.trim()) return;

    const userMsg = { role: 'user', content: text };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:7070/api/query', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          query: text,
          userContext: { // Hardcoded context for demo
            role: "member",
            allowed_plans: ["Plan X"],
            state: "CA"
          }
        })
      });

      const data = await response.json();
      const botMsg = { role: 'assistant', content: data.answer || "Sorry, I encountered an error." };

      setMessages(prev => [...prev, botMsg]);
    } catch (error) {
      console.error("Error sending message:", error);
      setMessages(prev => [...prev, { role: 'assistant', content: "Error: Could not connect to the backend." }]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <h1 style={{ textAlign: 'center', color: '#333' }}>Benefits Search Assistant</h1>

      <ChatBox messages={messages} isLoading={isLoading} />

      <PromptSuggestions onSelectPrompt={sendMessage} />

      <div style={{ display: 'flex', gap: '10px' }}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && sendMessage(input)}
          placeholder="Type your question..."
          style={{
            flexGrow: 1,
            padding: '12px',
            borderRadius: '4px',
            border: '1px solid #ccc',
            fontSize: '1rem'
          }}
          disabled={isLoading}
        />
        <button
          onClick={() => sendMessage(input)}
          disabled={isLoading}
          style={{
            padding: '12px 24px',
            backgroundColor: '#007bff',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: isLoading ? 'not-allowed' : 'pointer',
            fontSize: '1rem'
          }}
        >
          Send
        </button>
      </div>
    </div>
  );
}

export default App;
