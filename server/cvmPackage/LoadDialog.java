//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cvmPackage;

import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

public class LoadDialog extends JPanel {
    private JFileChooser fileChooser;

    public LoadDialog() {
        this.initComponents();
    }

    private void initComponents() {
        this.fileChooser = new JFileChooser();
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.fileChooser, -2, -1, -2).addContainerGap(-1, 32767)));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.fileChooser, -2, -1, -2).addContainerGap(-1, 32767)));
    }

    public JFileChooser getFileChooser() {
        return this.fileChooser;
    }
}
