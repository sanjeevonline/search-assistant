import React from 'react';

const PromptSuggestions = ({ onSelectPrompt }) => {
    const suggestions = [
        "What is the deductible for Plan X?",
        "How much is a Primary Care visit for Plan X?",
        "Is Urgent Care covered for Plan X?",
        "Compare Primary Care copay between Plan X and Plan Y"
    ];

    return (
        <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '20px' }}>
            {suggestions.map((prompt, index) => (
                <button
                    key={index}
                    onClick={() => onSelectPrompt(prompt)}
                    style={{
                        padding: '8px 12px',
                        borderRadius: '20px',
                        border: '1px solid #007bff',
                        backgroundColor: 'white',
                        color: '#007bff',
                        cursor: 'pointer',
                        fontSize: '0.9rem'
                    }}
                    onMouseOver={(e) => {
                        e.target.style.backgroundColor = '#007bff';
                        e.target.style.color = 'white';
                    }}
                    onMouseOut={(e) => {
                        e.target.style.backgroundColor = 'white';
                        e.target.style.color = '#007bff';
                    }}
                >
                    {prompt}
                </button>
            ))}
        </div>
    );
};

export default PromptSuggestions;
