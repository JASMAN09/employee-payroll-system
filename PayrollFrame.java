package com.payroll.ui;
import com.payroll.model.Employee; import com.payroll.service.PayrollService;
import javax.swing.*; import javax.swing.border.EmptyBorder; import javax.swing.table.DefaultTableModel; import java.awt.*; import java.sql.SQLException; import java.text.NumberFormat; import java.util.*;

public class PayrollFrame extends JFrame {
    private final PayrollService service=new PayrollService(); private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","Name","Email","Department","Designation","Basic (₹)","Allowances (₹)","Deductions (₹)","Net Salary (₹)"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable table=new JTable(model); private final NumberFormat money=NumberFormat.getCurrencyInstance(new Locale("en","IN"));
    private JTextField name,email,department,designation,basic,allowances,deductions; private JLabel status;
    private final Color ink=new Color(25,38,58), teal=new Color(0,139,123), pale=new Color(244,247,250);
    public PayrollFrame(){super("Employee Payroll Management System"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1180,740); setMinimumSize(new Dimension(980,650)); setLocationRelativeTo(null); getContentPane().setBackground(pale); build(); refresh();}
    private void build(){
        JPanel root=new JPanel(new BorderLayout(18,18)); root.setBorder(new EmptyBorder(22,24,18,24)); root.setBackground(pale); setContentPane(root);
        JPanel header=new JPanel(new BorderLayout()); header.setOpaque(false); JLabel title=new JLabel("Employee Payroll"); title.setFont(new Font("SansSerif",Font.BOLD,28)); title.setForeground(ink); JLabel sub=new JLabel("Manage your people. Calculate payroll. Stay organized."); sub.setForeground(new Color(105,119,137)); header.add(title,BorderLayout.NORTH); header.add(sub,BorderLayout.SOUTH); root.add(header,BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane(); tabs.setFont(new Font("SansSerif",Font.BOLD,13)); tabs.addTab("Employees",employeePanel()); tabs.addTab("Department Reports",reportPanel()); root.add(tabs,BorderLayout.CENTER);
        status=new JLabel("Ready"); status.setForeground(new Color(105,119,137)); root.add(status,BorderLayout.SOUTH);
    }
    private JPanel employeePanel(){JPanel panel=new JPanel(new BorderLayout(14,14)); panel.setOpaque(false); panel.setBorder(new EmptyBorder(12,0,0,0));
        JPanel form=new JPanel(new GridBagLayout()); form.setBackground(Color.WHITE); form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225,231,237)),new EmptyBorder(14,16,14,16)));
        name=field(); email=field(); department=field(); designation=field(); basic=field(); allowances=field(); deductions=field();
        addField(form,"Full name",name,0,0); addField(form,"Email",email,1,0); addField(form,"Department",department,2,0); addField(form,"Designation",designation,3,0); addField(form,"Basic salary (₹)",basic,0,1); addField(form,"Allowances (₹)",allowances,1,1); addField(form,"Deductions (₹)",deductions,2,1);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); actions.setOpaque(false); JButton save=button("Save employee",teal); JButton clear=button("Clear",new Color(100,116,139)); JButton delete=button("Delete selected",new Color(190,65,65)); actions.add(save);actions.add(clear);actions.add(delete);
        GridBagConstraints a=new GridBagConstraints();a.gridx=0;a.gridy=2;a.gridwidth=4;a.anchor=GridBagConstraints.WEST;a.insets=new Insets(12,0,0,0);form.add(actions,a);
        save.addActionListener(e->save());clear.addActionListener(e->clearForm());delete.addActionListener(e->delete());
        table.setRowHeight(28);table.setFont(new Font("SansSerif",Font.PLAIN,12));table.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting()&&table.getSelectedRow()>=0) loadSelected();});
        JScrollPane scroll=new JScrollPane(table);scroll.setBorder(BorderFactory.createLineBorder(new Color(225,231,237)));
        JLabel listTitle=new JLabel("Employee directory");listTitle.setFont(new Font("SansSerif",Font.BOLD,16));listTitle.setForeground(ink);
        JPanel lower=new JPanel(new BorderLayout(0,8));lower.setOpaque(false);lower.add(listTitle,BorderLayout.NORTH);lower.add(scroll,BorderLayout.CENTER);
        panel.add(form,BorderLayout.NORTH);panel.add(lower,BorderLayout.CENTER);return panel;}
    private JPanel reportPanel(){JPanel p=new JPanel(new BorderLayout(10,12));p.setBackground(Color.WHITE);p.setBorder(new EmptyBorder(20,20,20,20));JLabel heading=new JLabel("Department-wise payroll summary");heading.setFont(new Font("SansSerif",Font.BOLD,19));JTextArea out=new JTextArea();out.setEditable(false);out.setFont(new Font(Font.MONOSPACED,Font.PLAIN,14));out.setBorder(new EmptyBorder(12,12,12,12));JButton refresh=button("Generate report",teal);refresh.addActionListener(e->generateReport(out));p.add(heading,BorderLayout.NORTH);p.add(new JScrollPane(out),BorderLayout.CENTER);p.add(refresh,BorderLayout.SOUTH);generateReport(out);return p;}
    private JTextField field(){JTextField f=new JTextField(15);f.setFont(new Font("SansSerif",Font.PLAIN,13));return f;}
    private void addField(JPanel p,String label,JTextField f,int x,int y){GridBagConstraints c=new GridBagConstraints();c.gridx=x;c.gridy=y;c.weightx=1;c.fill=GridBagConstraints.HORIZONTAL;c.anchor=GridBagConstraints.WEST;c.insets=new Insets(4,5,7,12);JPanel cell=new JPanel(new BorderLayout(0,4));cell.setOpaque(false);JLabel l=new JLabel(label);l.setForeground(new Color(75,89,108));l.setFont(new Font("SansSerif",Font.BOLD,11));cell.add(l,BorderLayout.NORTH);cell.add(f,BorderLayout.CENTER);p.add(cell,c);}
    private JButton button(String text,Color color){JButton b=new JButton(text);b.setBackground(color);b.setForeground(Color.WHITE);b.setFocusPainted(false);b.setFont(new Font("SansSerif",Font.BOLD,12));b.setBorder(new EmptyBorder(9,14,9,14));return b;}
    private void save(){try{String n=name.getText().trim(),em=email.getText().trim(),dep=department.getText().trim(),des=designation.getText().trim();if(n.isEmpty()||em.isEmpty()||dep.isEmpty()||des.isEmpty())throw new IllegalArgumentException("Please fill in all employee details.");double bs=number(basic,"Basic salary"),al=number(allowances,"Allowances"),de=number(deductions,"Deductions");if(bs<0||al<0||de<0||de>bs+al)throw new IllegalArgumentException("Amounts must be non-negative and deductions cannot exceed gross salary.");int selected=table.getSelectedRow();int id=0;if(selected>=0){int row=table.convertRowIndexToModel(selected);id=(int)model.getValueAt(row,0);}Employee e=new Employee(id,n,em,dep,des,bs,al,de);service.saveEmployee(e);clearForm();refresh();status.setText("Employee saved successfully.");}catch(Exception ex){error(ex.getMessage());}}
    private double number(JTextField f,String label){try{return Double.parseDouble(f.getText().trim());}catch(Exception e){throw new IllegalArgumentException(label+" must be a valid number.");}}
    private void loadSelected(){int row=table.convertRowIndexToModel(table.getSelectedRow());name.setText(model.getValueAt(row,1).toString());email.setText(model.getValueAt(row,2).toString());department.setText(model.getValueAt(row,3).toString());designation.setText(model.getValueAt(row,4).toString());basic.setText(model.getValueAt(row,5).toString());allowances.setText(model.getValueAt(row,6).toString());deductions.setText(model.getValueAt(row,7).toString());}
    private void delete(){int selected=table.getSelectedRow();if(selected<0){error("Select an employee from the table first.");return;}int ok=JOptionPane.showConfirmDialog(this,"Delete this employee?","Confirm deletion",JOptionPane.YES_NO_OPTION);if(ok==JOptionPane.YES_OPTION)try{int row=table.convertRowIndexToModel(selected);service.deleteEmployee((int)model.getValueAt(row,0));clearForm();refresh();status.setText("Employee deleted.");}catch(SQLException ex){error(ex.getMessage());}}
    private void clearForm(){name.setText("");email.setText("");department.setText("");designation.setText("");basic.setText("");allowances.setText("");deductions.setText("");table.clearSelection();}
    private void refresh(){try{model.setRowCount(0);for(Employee e:service.getAllEmployees())model.addRow(new Object[]{e.getId(),e.getName(),e.getEmail(),e.getDepartment(),e.getDesignation(),e.getBasicSalary(),e.getAllowances(),e.getDeductions(),money.format(e.getNetSalary())});}catch(SQLException e){error("Database error: "+e.getMessage());}}
    private void generateReport(JTextArea out){
        try {
            var report=service.departmentReport();
            StringBuilder s=new StringBuilder("DEPARTMENT-WISE PAYROLL REPORT\n");
            s.append("=".repeat(95)).append("\n");
            s.append(String.format("%-22s %8s %15s %15s %15s %15s%n","Department","Staff","Basic total","Allowances","Deductions","Net payroll"));
            s.append("-".repeat(95)).append("\n");
            double total=0;
            for(var entry:report.entrySet()) {
                double[] v=entry.getValue(); total+=v[4];
                s.append(String.format(Locale.US,"%-22s %8.0f %15.2f %15.2f %15.2f %15.2f%n",entry.getKey(),v[0],v[1],v[2],v[3],v[4]));
            }
            s.append("-".repeat(95)).append("\nTOTAL NET PAYROLL: ").append(money.format(total));
            if(report.isEmpty()) s.append("\nNo employee records yet.");
            out.setText(s.toString());
        } catch(SQLException e) { out.setText("Could not generate report: "+e.getMessage()); }
    }
    private void error(String message){JOptionPane.showMessageDialog(this,message,"Payroll System",JOptionPane.ERROR_MESSAGE);}
}