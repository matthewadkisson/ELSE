/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class Editor extends javax.swing.JFrame {

   /**
    * Creates new form Editor
    */
   static void init(Scanner scanner) {
      scanner.init("0123456789", new NumberToken());
      scanner.init("abcdefghijklmnopqrstuvwxyz"
         + "ABCEDFGHIJKLMNOPQSTUVWXYZ", new IdToken());
      scanner.init("+", new PlusOpToken());
      scanner.init("-", new MinusOpToken());
      scanner.init("*", new MultOpToken());
      scanner.init("/", new DivideOpToken());
      scanner.init("(", new LeftParenToken());
      scanner.init(")", new RightParenToken());
      scanner.init(":", new AssignToken());
      scanner.init("\"", new StringToken());
      scanner.init(",", new CommaToken());
      Instruction.setScanner(scanner);
   }
   
   private static Listener svr;

   public Editor() {
      initComponents();
      
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jScrollPane1 = new javax.swing.JScrollPane();
      CodeArea = new javax.swing.JTextArea();
      jScrollPane2 = new javax.swing.JScrollPane();
      OutputArea = new javax.swing.JTextArea();
      jLabel1 = new javax.swing.JLabel();
      RunButton = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      CodeArea.setColumns(20);
      CodeArea.setRows(5);
      jScrollPane1.setViewportView(CodeArea);

      OutputArea.setColumns(20);
      OutputArea.setRows(5);
      jScrollPane2.setViewportView(OutputArea);

      jLabel1.setText("Output");

      RunButton.setText("Run");
      RunButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            RunButtonActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jLabel1)
                  .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addGroup(layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                     .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                     .addComponent(jScrollPane2))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                  .addComponent(RunButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(33, 33, 33))))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(RunButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   public void printOut(String s) {
      if (OutputArea.getText().equals("")) {
         OutputArea.setText(s);
      } else {
         OutputArea.setText(OutputArea.getText() + '\n' + s);
      }
   }

   private void RunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunButtonActionPerformed
      OutputArea.setText(null);
      Instruction.setEditor(this);

      System.setProperty("java.rmi.server.hostname", "localhost");

      try {

         //setup mailbox
         Map<String, LinkedList<String>> mailbox = new HashMap<>();
         LinkedList ll = new LinkedList<>();
         ll.add("0");
         mailbox.put("toasty", ll);
         Instruction.setMailbox(mailbox);
         svr.setMailbox(mailbox);

         //setup listener
         Listener svr = new Listener();
         svr.setMailbox(mailbox);
         svr.startRegistry();
         svr.registerObject("Listener", svr);

         // Initialize scanner to process various tokens
         Scanner scanner = new Scanner();
         init(scanner);

         scanner.setSource(CodeArea.getText());

         // Load the first token into the scanner
         scanner.get();

         // Initialize the run-time environment to have some entries
         Environment env = new Environment();

         env.put("qw", "chickenTenders");
         Instruction.setEnvironment(env);

         // Parse the program 
         Stmt instr = StmtList.parse();

         // Execute the program
         instr.eval();
      } catch (Exception e) {
         System.out.println(e);
      }

   }//GEN-LAST:event_RunButtonActionPerformed

   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {
      /* Set the Nimbus look and feel */
      //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
       * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (InstantiationException ex) {
         java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
         java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            new Editor().setVisible(true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JTextArea CodeArea;
   private javax.swing.JTextArea OutputArea;
   private javax.swing.JButton RunButton;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   // End of variables declaration//GEN-END:variables
}
