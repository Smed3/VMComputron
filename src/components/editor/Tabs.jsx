import styles from './Tabs.module.css';
import {FiPlus, FiPlay, FiSkipBack, FiSkipForward, FiRotateCw} from 'react-icons/fi';

const Tabs = ({ tabs, setTabs, activeTabId,setActiveTabId}) => {
    const addTab = () => {
        const newId = Date.now();
        setTabs([...tabs, { id: newId, name: `Tab ${tabs.length + 1}`, content: "" }]);
        setActiveTabId(newId);
    };

    const closeTab = (id) => {
        const filtered = tabs.filter(t => t.id !== id);
        setTabs(filtered);
        if (activeTabId === id && filtered.length > 0) {
            setActiveTabId(filtered[0].id);
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
                                className={`${styles.TabButton} ${tab.id === activeTabId ? styles.TabActive : styles.TabInactive} ${index === tabs.length - 1 ? styles.TabInactiveLast : ''}`}
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
                        // onClick={() => onRun?.(activeTab?.content)}
                        className={styles.ControlButton}
                        aria-label="Reset">
                        <FiRotateCw />
                    </button>
                    <button
                        // onClick={() => onRun?.(activeTab?.content)}
                        className={styles.ControlButton}
                        aria-label="Step backward">
                        <FiSkipBack />
                    </button>
                    <button
                        // onClick={() => onNavBack?.(activeTab?.content)}
                        className={styles.ControlButton}
                        aria-label="Play">
                        <FiPlay />
                    </button>
                    <button
                        // onClick={() => onNavForward?.(activeTab?.content)}
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