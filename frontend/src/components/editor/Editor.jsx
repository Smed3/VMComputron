import Tabs from "./Tabs.jsx";
import styles from './Editor.module.css';
import InstructionsInput from "./InstructionsInput.jsx";
import {useState} from "react";
import { useServerContext } from "../../contexts/ServerContext";


const Editor = ({ setEditorFilter }) => {
    const {
        vmReset,
        vmBack,
        vmForward,
        runProgram
    } = useServerContext();

    const [busy, setBusy] = useState(false);
    const [error, setError] = useState(null);

    const safe = (fn) => async (...args) => {
        if (busy) return;
        setBusy(true);
        setError(null);

        try {
            await fn(...args);
        } catch (e) {
            setError(e.message || "VM error");
        } finally {
            setBusy(false);
        }
    };

    const [tabs, setTabs] = useState([{ id: 1, name: "Tab 1", content: "" }]);
    const [activeTabId, setActiveTabId] = useState(1);
    const activeTab = tabs.find(t => t.id === activeTabId);

    const updateTabContent = (newContent) => {
        setTabs(tabs.map(t =>
            t.id === activeTabId ? { ...t, content: newContent } : t
        ));
    };

    const onRun = async () => {
        if (!activeTab?.content) return;

        try {
            await runProgram({
                code: activeTab.content,
                runAfterLoad: true,
            });
        } catch (e) {
            setError(e.message);
        }
    };

    return (
        <div className={styles.EditorContainer}>
            <Tabs tabs={tabs}
                  setTabs={setTabs}
                  activeTabId={activeTabId}
                  setActiveTabId={setActiveTabId}
                  onReset={safe(vmReset)}
                  onRun={onRun}
                  onBack={safe(vmBack)}
                  onForward={safe(vmForward)}
                  disabled={busy}
            />
            <InstructionsInput activeTab={activeTab}
                               onChange={updateTabContent}
                               onType={setEditorFilter}
            />
        </div>
    );
}

export default Editor;