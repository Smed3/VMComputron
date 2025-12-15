import styles from './Tabs.module.css';
import {FiPlus, FiPlay, FiSkipBack, FiSkipForward, FiRotateCw} from 'react-icons/fi';

const Tabs = ({ tabs, activeTabId, setActiveTabId, onAddTab, onCloseTab, onReset, onRun, onBack, onForward, disabled}) => {

    return (
        <div className={styles.TabsContainer}>
            <div className={styles.TabsHeader}>
                <div className={styles.TabsSpace}>
                    <div className={styles.TabsList}>
                        {tabs.map((tab, index) => (
                            <button
                                key={tab.id}
                                role="tab"
                                aria-selected={tab.id === activeTabId}
                                aria-controls={`tabpanel-${tab.id}`}
                                className={`${styles.TabButton} ${tab.id === activeTabId ? styles.TabActive : styles.TabInactive}`}
                                onClick={() => setActiveTabId(tab.id)}
                            >
                                {tab.name}
                                <span className={styles.TabClose} onClick={(e) => { e.stopPropagation(); onCloseTab(tab.id); }}>×</span>
                            </button>
                        ))}
                    </div>
                    <button className={styles.TabAddButton} onClick={onAddTab}>
                        <FiPlus />
                    </button>
                </div>

                <div className={styles.TabsControls}>
                    <button
                        onClick={onReset}
                        disabled={disabled}
                        className={styles.ControlButton}
                        aria-label="Reset">
                        <FiRotateCw />
                    </button>
                    <button
                        onClick={onBack}
                        disabled={disabled}
                        className={styles.ControlButton}
                        aria-label="Step backward">
                        <FiSkipBack />
                    </button>
                    <button
                        onClick={onRun}
                        disabled={disabled}
                        className={styles.ControlButton}
                        aria-label="Play">
                        <FiPlay />
                    </button>
                    <button
                        onClick={onForward}
                        disabled={disabled}
                        className={styles.ControlButton}
                        aria-label="Step forward">
                        <FiSkipForward />
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Tabs;