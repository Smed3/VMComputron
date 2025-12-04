import Tabs from "./Tabs.jsx";
import styles from './Editor.module.css';
import InstructionsInput from "./InstructionsInput.jsx";
import {useState} from "react";


const Editor = ({ setEditorFilter }) => {
    const [tabs, setTabs] = useState([{ id: 1, name: "Tab 1", content: "" }]);
    const [activeTabId, setActiveTabId] = useState(1);
    const activeTab = tabs.find(t => t.id === activeTabId);

    const updateTabContent = (newContent) => {
        setTabs(tabs.map(t =>
            t.id === activeTabId ? { ...t, content: newContent } : t
        ));
    };

    return (
        <div className={styles.EditorContainer}>
            <Tabs tabs={tabs} setTabs={setTabs} activeTabId={activeTabId} setActiveTabId={setActiveTabId}/>
            <InstructionsInput activeTab={activeTab} onChange={updateTabContent} onType={setEditorFilter}/>
        </div>
    );
}

export default Editor;