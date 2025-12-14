import styles from './Tabs.module.css';
import {FiPlus, FiPlay, FiSkipBack, FiSkipForward, FiRotateCw} from 'react-icons/fi';
import { v4 as uuidv4 } from 'uuid';

const Tabs = ({ tabs, setTabs, activeTabId,setActiveTabId, onReset, onRun, onBack, onForward, disabled}) => {
    const addTab = () => {
        const newId = uuidv4();
        setTabs([...tabs, { id: newId, name: `Tab ${tabs.length + 1}`, content: "" }]);
        setActiveTabId(newId);
    };

    const closeTab = (id) => {
        const filtered = tabs.filter(t => t.id !== id);
        setTabs(filtered);
        if (activeTabId === id) {
            setActiveTabId(filtered.length > 0 ? filtered[0].id : null);
        }
    };

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
                                <span className={styles.TabClose} onClick={(e) => { e.stopPropagation(); closeTab(tab.id); }}>×</span>
                            </button>
                        ))}
                    </div>
                    <button className={styles.TabAddButton} onClick={addTab}>
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