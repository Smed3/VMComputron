import { useContext, useState } from 'react';
import styles from './Header.module.css';
import { ThemeContext } from '../../contexts/theme-context';
import Sun from './icons/sun';
import Moon from './icons/Moon';

function Header() {
  const [openMenu, setOpenMenu] = useState(null);

  const { theme, toggleTheme } = useContext(ThemeContext);

  const toggleMenu = (menuName) => {
    setOpenMenu(openMenu === menuName ? null : menuName);
  };

  const closeMenu = () => {
    setOpenMenu(null);
  };

  const handleFileAction = (action) => {
    console.log(`File action: ${action}`);
    switch(action) {
      case 'new':
        alert('New File: Create a new VM program');
        break;
      case 'open':
        alert('Open File: Load an existing VM program');
        break;
      case 'save':
        alert('Save: Save current program');
        break;
      case 'saveAs':
        alert('Save As: Save program with new name');
        break;
      case 'export':
        alert('Export: Export program as binary');
        break;
      default:
        break;
    }
    closeMenu();
  };

  const handleSettingsAction = (action) => {
    console.log(`Settings action: ${action}`);
    switch(action) {
      case 'preferences':
        alert('Preferences: Configure IDE settings');
        break;
      case 'theme':
        toggleTheme();
        // alert('Theme: Change color scheme');
        break;
      case 'vmConfig':
        alert('VM Configuration: Set memory size, clock speed');
        break;
      case 'keyboardShortcuts':
        alert('Keyboard Shortcuts: Customize hotkeys');
        break;
      default:
        break;
    }
    closeMenu();
  };

  const handleViewAction = (action) => {
    console.log(`View action: ${action}`);
    switch(action) {
      case 'toggleConsole':
        alert('Toggle Console: Show/hide console window');
        break;
      case 'toggleMemory':
        alert('Toggle Memory: Show/hide memory view');
        break;
      case 'toggleInstructions':
        alert('Toggle Instructions: Show/hide instruction table');
        break;
      case 'resetLayout':
        alert('Reset Layout: Restore default window arrangement');
        break;
      case 'fullscreen':
        if (document.fullscreenElement) {
          document.exitFullscreen();
        } else {
          document.documentElement.requestFullscreen();
        }
        break;
      default:
        break;
    }
    closeMenu();
  };

  const handleHelpAction = (action) => {
    console.log(`Help action: ${action}`);
    switch(action) {
      case 'documentation':
        window.open('https://github.com/Smed3/VMComputron', '_blank');
        break;
      case 'instructionSet':
        alert('Instruction Set: View complete VM instruction reference');
        break;
      case 'examples':
        alert('Examples: Load sample VM programs');
        break;
      case 'about':
        alert('VMComputron v1.0\nA virtual machine simulator and IDE\n© 2025');
        break;
      default:
        break;
    }
    closeMenu();
  };

  return <>
    <header className={styles.header} onMouseLeave={closeMenu}>
      <div className={styles.title}>VMComputron IDE</div>
      
      <nav className={styles.menuBar}>
        {/* File Menu */}
        <div className={styles.menuItem}>
          <button 
            className={`${styles.menuButton} ${openMenu === 'file' ? styles.active : ''}`}
            onClick={() => toggleMenu('file')}
          >
            File
          </button>
          {openMenu === 'file' && (
            <div className={styles.dropdown}>
              <button onClick={() => handleFileAction('new')}>
                New <span className={styles.shortcut}>Ctrl+N</span>
              </button>
              <button onClick={() => handleFileAction('open')}>
                Open <span className={styles.shortcut}>Ctrl+O</span>
              </button>
              <div className={styles.divider}></div>
              <button onClick={() => handleFileAction('save')}>
                Save <span className={styles.shortcut}>Ctrl+S</span>
              </button>
              <button onClick={() => handleFileAction('saveAs')}>
                Save As <span className={styles.shortcut}>Ctrl+Shift+S</span>
              </button>
              <div className={styles.divider}></div>
              <button onClick={() => handleFileAction('export')}>
                Export Binary
              </button>
            </div>
          )}
        </div>

        {/* Settings Menu */}
        <div className={styles.menuItem}>
          <button 
            className={`${styles.menuButton} ${openMenu === 'settings' ? styles.active : ''}`}
            onClick={() => toggleMenu('settings')}
          >
            Settings
          </button>
          {openMenu === 'settings' && (
            <div className={styles.dropdown}>
              <button onClick={() => handleSettingsAction('preferences')}>
                Preferences
              </button>
              <button onClick={() => handleSettingsAction('theme')}>
                Theme {theme == 'light' ? <Sun/> : <Moon/>}
              </button>
              <div className={styles.divider}></div>
              <button onClick={() => handleSettingsAction('vmConfig')}>
                VM Configuration
              </button>
              <button onClick={() => handleSettingsAction('keyboardShortcuts')}>
                Keyboard Shortcuts
              </button>
            </div>
          )}
        </div>

        {/* View Menu */}
        <div className={styles.menuItem}>
          <button 
            className={`${styles.menuButton} ${openMenu === 'view' ? styles.active : ''}`}
            onClick={() => toggleMenu('view')}
          >
            View
          </button>
          {openMenu === 'view' && (
            <div className={styles.dropdown}>
              <button onClick={() => handleViewAction('toggleConsole')}>
                Toggle Console <span className={styles.shortcut}>Ctrl+`</span>
              </button>
              <button onClick={() => handleViewAction('toggleMemory')}>
                Toggle Memory View
              </button>
              <button onClick={() => handleViewAction('toggleInstructions')}>
                Toggle Instructions
              </button>
              <div className={styles.divider}></div>
              <button onClick={() => handleViewAction('resetLayout')}>
                Reset Layout
              </button>
              <button onClick={() => handleViewAction('fullscreen')}>
                Fullscreen <span className={styles.shortcut}>F11</span>
              </button>
            </div>
          )}
        </div>

        {/* Help Menu */}
        <div className={styles.menuItem}>
          <button 
            className={`${styles.menuButton} ${openMenu === 'help' ? styles.active : ''}`}
            onClick={() => toggleMenu('help')}
          >
            Help
          </button>
          {openMenu === 'help' && (
            <div className={styles.dropdown}>
              <button onClick={() => handleHelpAction('documentation')}>
                Documentation
              </button>
              <button onClick={() => handleHelpAction('instructionSet')}>
                Instruction Set Reference
              </button>
              <button onClick={() => handleHelpAction('examples')}>
                Example Programs
              </button>
              <div className={styles.divider}></div>
              <button onClick={() => handleHelpAction('about')}>
                About VMComputron
              </button>
            </div>
          )}
        </div>
      </nav>
    </header>
  </>
}

export default Header;