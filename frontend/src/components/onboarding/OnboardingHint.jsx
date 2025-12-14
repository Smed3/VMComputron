import styles from "./OnboardingHint.module.css";

export default function OnboardingHint({ onClose }) {
    return (
        <div
            className={styles.overlay}
            role="dialog"
            aria-modal="true"
            onClick={onClose}
        >
            <div
                className={styles.card}
                onClick={(e) => e.stopPropagation()}
            >
                <div className={styles.header}>
                    <div className={styles.title}>Vitajte v Computron VM</div>

                    <button
                        className={styles.close}
                        onClick={onClose}
                        aria-label="Zatvoriť"
                        type="button"
                    >
                        ×
                    </button>
                </div>

                <div className={styles.text}>
                    <ol className={styles.list}>
                        <li>Napíšte program alebo nahrajte súbor cez <b>Load</b></li>
                        <li>Spustite: <b>Run</b> alebo <b>Step</b></li>
                        <li>Sledujte: registre (PC/A/…) a RAM vpravo</li>
                        <li>stiahnite si súbor pomocou príkazu <b>Store</b></li>
                    </ol>

                    <div className={styles.exampleTitle}>Príklad programu:</div>
                    <pre className={styles.example}>
{`INP
ADDM 5
OUT
EXIT`}
          </pre>

                    <div className={styles.tip}>
                        Ak sa zobrazí „Waiting input“, zadajte číslo a stlačte tlačidlo <b>Step</b> ešte raz.
                    </div>

                    <div className={styles.actions}>
                        <button className={styles.primaryBtn} onClick={onClose} type="button">
                            Rozumiem
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
}
