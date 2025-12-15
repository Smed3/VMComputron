import styles from "./InstructionHints.module.css";

export default function InstructionHints({ items, onSelect }) {
    if (!items.length) return null;

    return (
        <div className={styles.popup}>
            {items.map((i) => (
                <div
                    key={i.code}
                    className={styles.item}
                    onMouseDown={() => onSelect(i)}
                >
                    <b className={styles.instr}>{i.instr}</b>
                    <span className={styles.code}>{i.code}</span>
                    <span className={styles.attr}>{i.attr}</span>
                </div>
            ))}
        </div>
    );
}
