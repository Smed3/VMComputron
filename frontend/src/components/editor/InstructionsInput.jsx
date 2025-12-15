import styles from './InstructionsInput.module.css';
import {INSTRUCTIONS} from './instructions.js';
import InstructionHints from "./InstructionsHints.jsx";
import {useRef, useState} from 'react';

const InstructionsInput = ({ activeTab, onChange }) => {
    if (!activeTab) return null;

    const lines = activeTab.content ? activeTab.content.split('\n') : [''];

    // eslint-disable-next-line react-hooks/rules-of-hooks
    const textareaRef = useRef(null);
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [hints, setHints] = useState([]);
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [caret, setCaret] = useState({ x: 0, y: 0 });

    const handleChange = (e) => {
        const value = e.target.value;
        onChange(value);

        const pos = e.target.selectionStart;
        const lines = value.slice(0, pos).split("\n");
        const currentLine = lines.at(-1).trim().toUpperCase();

        if (!currentLine) {
            setHints([]);
            return;
        }

        const filtered = INSTRUCTIONS.filter(i =>
            i.instr.startsWith(currentLine)
        );

        setHints(filtered);

        const textarea = textareaRef.current;
        const coords = getCaretCoordinates(textarea);
        setCaret(coords);
    };

    const getCaretCoordinates = (textarea) => {
        const div = document.createElement("div");
        const span = document.createElement("span");

        const style = getComputedStyle(textarea);
        for (const prop of style) {
            div.style[prop] = style[prop];
        }

        div.style.position = "absolute";
        div.style.visibility = "hidden";
        div.style.whiteSpace = "pre-wrap";
        div.style.wordWrap = "break-word";
        div.style.width = textarea.offsetWidth + "px";

        div.textContent = textarea.value.substring(0, textarea.selectionStart);
        span.textContent = ".";

        div.appendChild(span);
        document.body.appendChild(div);

        const { left, top, height } = span.getBoundingClientRect();
        document.body.removeChild(div);

        return {
            x: left,
            y: top + height
        };
    };

    const insertInstruction = (instr) => {
        const textarea = textareaRef.current;
        const pos = textarea.selectionStart;
        const text = textarea.value;

        const before = text.slice(0, pos).replace(/[A-Z]*$/, instr.instr + " ");
        const after = text.slice(pos);

        onChange(before + after);
        setHints([]);
    };

    return (
        <div className={styles.InstructionsContainer} style={{ position: "relative" }}>
            <div className={styles.LineNumbers}>
                {lines.map((_, idx) => (
                    <div key={idx} className={styles.LineNumber}>
                        {idx + 1}
                    </div>
                ))}
            </div>
            <textarea
                ref={textareaRef}
                className={styles.InstructionsMainArea}
                value={activeTab.content}
                onChange={handleChange}
            />

            <div
                style={{
                    position: "fixed",
                    left: caret.x + 30,
                    top: caret.y - 30
                }}
            >
                <InstructionHints
                    items={hints}
                    onSelect={insertInstruction}
                />
            </div>
        </div>
    );
};

export default InstructionsInput;