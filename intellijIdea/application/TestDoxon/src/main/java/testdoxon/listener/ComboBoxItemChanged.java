package testdoxon.listener;

import testdoxon.model.TestFile;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ComboBoxItemChanged implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object item = e.getItem();
        if(e.getStateChange() == 1) {
            if(item instanceof TestFile) {
                TestFile selected = (TestFile) item;
                System.out.println(selected.getFilepath());

                // Update JList with method from this test class
            }
        }
    }

}
