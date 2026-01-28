import React from 'react';

const ChatBox = ({ messages, isLoading }) => {
    return (
        <div style={{
            border: '1px solid #ccc',
            borderRadius: '8px',
            height: '400px',
            overflowY: 'scroll',
            padding: '20px',
            marginBottom: '20px',
            display: 'flex',
            flexDirection: 'column',
            gap: '10px',
            backgroundColor: '#f9f9f9'
        }}>
            {messages.map((msg, index) => (
                <div key={index} style={{
                    alignSelf: msg.role === 'user' ? 'flex-end' : 'flex-start',
                    backgroundColor: msg.role === 'user' ? '#007bff' : '#e9ecef',
                    color: msg.role === 'user' ? 'white' : 'black',
                    padding: '10px 15px',
                    borderRadius: '15px',
                    maxWidth: '80%',
                    wordWrap: 'break-word',
                    whiteSpace: 'pre-wrap'
                }}>
                    <strong>{msg.role === 'user' ? 'You' : 'Assistant'}:</strong>
                    <br />
                    {msg.content}
                </div>
            ))}
            {isLoading && (
                <div style={{ alignSelf: 'flex-start', color: '#888', fontStyle: 'italic' }}>
                    Thinking...
                </div>
            )}
        </div>
    );
};

export default ChatBox;
