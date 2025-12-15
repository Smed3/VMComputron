import { createContext, useContext, useMemo, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { useServerContext } from './ServerContext';

const EditorContext = createContext(null);

const makeUntitledTab = (index) => ({
  id: uuidv4(),
  name: `Untitled ${index}`,
  filename: `untitled-${index}.cvm`,
  content: '',
});

export function EditorProvider({ children }) {
  const { setActiveCode } = useServerContext();

  const initialTab = makeUntitledTab(1);
  const [tabs, setTabs] = useState([initialTab]);
  const [activeTabId, setActiveTabId] = useState(initialTab.id);

  const activeTab = useMemo(
    () => tabs.find((t) => t.id === activeTabId) || tabs[0] || null,
    [tabs, activeTabId]
  );

  const updateTabContent = (content) => {
    if (!activeTab) return;
    setTabs((prev) =>
      prev.map((t) => (t.id === activeTab.id ? { ...t, content } : t))
    );
    setActiveCode?.(content);
  };

  const addTab = () => {
    setTabs((prev) => {
      const nextIndex = prev.length + 1;
      const newTab = makeUntitledTab(nextIndex);
      setActiveTabId(newTab.id);
      return [...prev, newTab];
    });
  };

  const closeTab = (id) => {
    setTabs((prev) => {
      const filtered = prev.filter((t) => t.id !== id);
      if (!filtered.length) {
        const freshTab = makeUntitledTab(1);
        setActiveTabId(freshTab.id);
        return [freshTab];
      }
      if (activeTabId === id) {
        setActiveTabId(filtered[0].id);
      }
      return filtered;
    });
  };

  const renameActiveTab = (name, filename) => {
    if (!activeTab) return;
    setTabs((prev) =>
      prev.map((t) =>
        t.id === activeTab.id ? { ...t, name: name ?? t.name, filename: filename ?? t.filename } : t
      )
    );
  };

  const downloadFile = (content, filename) => {
    const blob = new Blob([content ?? ''], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename || 'program.cvm';
    a.click();
    URL.revokeObjectURL(url);
  };

  const saveFile = () => {
    if (!activeTab) return;
    const filename = activeTab.filename || `${activeTab.name}.cvm`;
    downloadFile(activeTab.content ?? '', filename);
  };

  const saveFileAs = () => {
    if (!activeTab) return;
    const suggested = activeTab.filename || `${activeTab.name}.cvm`;
    const input = window.prompt('Save as', suggested);
    if (!input) return;
    const filename = input.trim();
    const finalName = filename.includes('.') ? filename : `${filename}.cvm`;
    renameActiveTab(finalName.replace(/\.[^.]+$/, ''), finalName);
    downloadFile(activeTab.content ?? '', finalName);
  };

  const newFile = () => {
    addTab();
  };

  return (
    <EditorContext.Provider
      value={{
        tabs,
        activeTabId,
        setActiveTabId,
        activeTab,
        updateTabContent,
        addTab,
        closeTab,
        newFile,
        saveFile,
        saveFileAs,
        renameActiveTab,
      }}
    >
      {children}
    </EditorContext.Provider>
  );
}

export const useEditorContext = () => useContext(EditorContext);
