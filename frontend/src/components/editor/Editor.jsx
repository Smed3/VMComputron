import Tabs from "./Tabs.jsx";
import styles from './Editor.module.css';
import InstructionsInput from "./InstructionsInput.jsx";
import { useState } from "react";
import { useServerContext } from "../../contexts/ServerContext";
import { useEditorContext } from "../../contexts/EditorContext.jsx";


const Editor = () => {
    const {
        vmReset,
        vmBack,
        vmForward,
        runProgram
    } = useServerContext();
    const {
        tabs,
        activeTabId,
        setActiveTabId,
        activeTab,
        updateTabContent,
        addTab,
        closeTab,
    } = useEditorContext();

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
                  activeTabId={activeTabId}
                  setActiveTabId={setActiveTabId}
                onAddTab={addTab}
                onCloseTab={closeTab}
                  onReset={safe(vmReset)}
                  onRun={onRun}
                  onBack={safe(vmBack)}
                  onForward={safe(vmForward)}
                  disabled={busy}
            />
            <InstructionsInput activeTab={activeTab}
                               onChange={updateTabContent}
                               // onType={}
            />
        </div>
    );
}

export default Editor;