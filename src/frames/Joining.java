package frames;

import frames.dialog.ParGrup2;
import frames.dialog.ParGrup2b;
import frames.dialog.ParColor2;
import frames.dialog.ParGrup2a;
import frames.dialog.DicArtikl;
import common.DialogListener;
import common.EditorListener;
import common.FrameToFile;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eJoindet;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinpar2;
import domain.eParams;
import domain.eSysprof;
import enums.Enam;
import enums.ParamList;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import frames.swing.DefTableModel;
import frames.dialog.DicJoinvar;
import domain.eJoinvar;
import frames.swing.BooleanRenderer;
import common.Util;
import frames.dialog.DicColvar;
import enums.UseColcalc;
import java.awt.Component;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import startup.Main;

//варианты соединений
public class Joining extends javax.swing.JFrame {

    private ImageIcon icon[] = {
        new ImageIcon(getClass().getResource("/resource/img16/b000.gif")),
        new ImageIcon(getClass().getResource("/resource/img16/b001.gif")),
        new ImageIcon(getClass().getResource("/resource/img16/b002.gif")),
        new ImageIcon(getClass().getResource("/resource/img16/b003.gif")),
        new ImageIcon(getClass().getResource("/resource/img16/b004.gif")),
        new ImageIcon(getClass().getResource("/resource/img16/b005.gif"))};
    private int[] indexIcon = {10, 20, 30, 31, 40, 41};

    private Query qParams = new Query(eParams.id, eParams.grup, eParams.numb, eParams.text);
    private Query qColor = new Query(eColor.id, eColor.colgrp_id, eColor.name);
    private Query qArtikl = new Query(eArtikl.id, eArtikl.code, eArtikl.name);
    private Query qJoining = new Query(eJoining.values());
    private Query qJoinvar = new Query(eJoinvar.values());
    private Query qJoindet = new Query(eJoindet.values());
    private Query qJoinpar1 = new Query(eJoinpar1.values());
    private Query qJoinpar2 = new Query(eJoinpar2.values());
    private String subsql = "";
    private Window owner = null;
    private EditorListener listenerEditor;
    private DialogListener listenerArtikl, listenerPar1, listenerPar2, listenerJoinvar, listenerColor, listenerColvar;

    public Joining() {
        initComponents();
        initElements();
        loadingData();
        listenerCell();
        listenerDict();
        loadingModel();
    }
    
    public Joining(java.awt.Window owner, Set<Object> keys) {
        this.owner = owner;
        this.subsql = keys.stream().map(pk -> String.valueOf(pk)).collect(Collectors.joining(",", "(", ")"));
        initComponents();
        initElements();
        loadingData();
        listenerCell();
        listenerDict();
        loadingModel();
        //owner.setEnabled(false);
    }

    private void loadingData() {

        tab1.setToolTipText("");
        qParams.select(eParams.up, "where", eParams.joint, "= 1 and", eParams.numb, "= 0 order by", eParams.text);
        qColor.select(eColor.up);
        qArtikl.select(eArtikl.up);
        if (owner == null) {
            qJoining.select(eJoining.up, "order by", eJoining.name);
        } else {
            qJoining.select(eJoining.up, "where", eJoining.id, "in", subsql);
        }
    }

    private void loadingModel() {
        new DefTableModel(tab1, qJoining, eJoining.artikl_id1, eJoining.artikl_id2, eJoining.name, eJoining.main, eJoining.analog) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (eJoining.artikl_id1 == field) {
                    return qArtikl.stream().filter(rec -> val.equals(rec.get(eArtikl.id))).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);

                } else if (eJoining.artikl_id2 == field) {
                    return qArtikl.stream().filter(rec -> val.equals(rec.get(eArtikl.id))).findFirst().orElse(eArtikl.up.newRecord()).get(eArtikl.code);
                }
                return val;
            }
        };
        new DefTableModel(tab2, qJoinvar, eJoinvar.prio, eJoinvar.name, eJoinvar.mirr);
        new DefTableModel(tab3, qJoinpar1, eJoinpar1.grup, eJoinpar1.text) {

            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 1) {
                    setValueAt(aValue, rowIndex, eJoinpar1.text);
                }
            }

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eJoinpar1.grup == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eJoinpar1.grup) + "-" + record.getStr(eJoinpar1.text) : record.getStr(eJoinpar1.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab4, qJoindet, eJoindet.artikl_id, eJoindet.artikl_id, eJoindet.color_fk, eJoindet.types) {

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (eJoindet.artikl_id == field) {
                    int id = Integer.valueOf(val.toString());
                    Record recordArt = qArtikl.stream().filter(rec -> rec.getInt(eArtikl.id) == id).findFirst().orElse(eArtikl.up.newRecord());
                    if (col == 0) {
                        return recordArt.getStr(eArtikl.code);
                    } else if (col == 1) {
                        return recordArt.getStr(eArtikl.name);
                    }
                } else if (eJoindet.color_fk == field) {
                    int colorFk = Integer.valueOf(val.toString());

                    if (Integer.valueOf(UseColcalc.automatic[0]) == colorFk) {
                        return UseColcalc.automatic[1];
                    } else if (Integer.valueOf(UseColcalc.precision[0]) == colorFk) {
                        return UseColcalc.precision[1];
                    }
                    if (colorFk > 0) {
                        return qColor.stream().filter(rec -> rec.getInt(eColor.id) == colorFk).findFirst().orElse(eColor.up.newRecord()).get(eColor.name);
                    } else {
                        return qParams.stream().filter(rec -> rec.getInt(eParams.grup) == colorFk).findFirst().orElse(eParams.up.newRecord()).get(eParams.text);
                    }
                } else if (eJoindet.types == field) {

                    int types = Integer.valueOf(val.toString());
                    if (UseColcalc.P00.find(types) != null) {
                        return UseColcalc.P00.find(types).text();
                    } else {
                        return null;
                    }
                }
                return val;
            }
        };
        new DefTableModel(tab5, qJoinpar2, eJoinpar2.grup, eJoinpar2.text) {

            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 1) {
                    setValueAt(aValue, rowIndex, eJoinpar2.text);
                }
            }

            public Object getValueAt(int col, int row, Object val) {
                Field field = columns[col];
                if (val != null && eJoinpar2.grup == field) {
                    if (Integer.valueOf(String.valueOf(val)) < 0) {
                        Record record = qParams.stream().filter(rec -> rec.get(eParams.grup).equals(val)).findFirst().orElse(eParams.up.newRecord());
                        return (Main.dev) ? record.getStr(eJoinpar2.grup) + "-" + record.getStr(eJoinpar2.text) : record.getStr(eJoinpar2.text);
                    } else {
                        Enam en = ParamList.find(Integer.valueOf(val.toString()));
                        return (Main.dev) ? en.numb() + "-" + en.text() : en.text();
                    }
                }
                return val;
            }
        };

        tab2.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0) {
                    int types = qJoinvar.get(row).getInt(eJoinvar.types);
                    JLabel label = (JLabel) comp;
                    for (int i = 0; i < 6; i++) {
                        if (types == indexIcon[i]) {
                            label.setIcon(icon[i]);
                        }
                    }
                }
                return comp;
            }
        });
        tab2.getColumnModel().getColumn(2).setCellRenderer(new BooleanRenderer());

        Util.buttonEditorCell(tab1, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab1, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1);
        });

        Util.buttonEditorCell(tab2, 1).addActionListener(event -> {
            DicJoinvar frame = new DicJoinvar(this, listenerJoinvar);
        });

        Util.buttonEditorCell(tab3, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab2);
            if (row != -1) {
                Record record = qJoinvar.get(row);
                int joinVar = record.getInt(eJoinvar.types);
                ParGrup2 frame = new ParGrup2(this, listenerPar1, eParams.joint, joinVar * 100);
            }
        });

        Util.buttonEditorCell(tab3, 1, listenerEditor).addActionListener(event -> {
            Record record = qJoinpar1.get(Util.getSelectedRec(tab3));
            int grup = record.getInt(eJoinpar1.grup);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar1, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar1, list);
            }
        });

        Util.buttonEditorCell(tab4, 0).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
        });

        Util.buttonEditorCell(tab4, 1).addActionListener(event -> {
            DicArtikl frame = new DicArtikl(this, listenerArtikl, 1, 2, 3, 4);
        });

        Util.buttonEditorCell(tab4, 2).addActionListener(event -> {
            Record record = qJoindet.get(Util.getSelectedRec(tab4));
            int artikl_id = record.getInt(eJoindet.artikl_id);
            ParColor2 frame = new ParColor2(this, listenerColor, artikl_id);
        });

        Util.buttonEditorCell(tab4, 3).addActionListener(event -> {
            Record record = qJoindet.get(Util.getSelectedRec(tab4));
            int colorFk = record.getInt(eJoindet.color_fk);
            DicColvar frame = new DicColvar(this, listenerColvar, colorFk);
        });

        Util.buttonEditorCell(tab5, 0).addActionListener(event -> {
            int row = Util.getSelectedRec(tab4);
            if (row != -1) {
                Record recordJoin = qJoindet.get(row);
                int artikl_id = recordJoin.getInt(eJoindet.artikl_id);
                Record recordArt = eArtikl.find(artikl_id, false);
                int level = recordArt.getInt(eArtikl.level1);
                Integer[] part = {0, 12000, 11000, 12000, 11000, 0};
                ParGrup2 frame = new ParGrup2(this, listenerPar2, eParams.joint, part[level]);
            }
        });

        Util.buttonEditorCell(tab5, 1, listenerEditor).addActionListener(event -> {
            Record record = qJoinpar2.get(Util.getSelectedRec(tab5));
            int grup = record.getInt(eJoinpar2.grup);
            if (grup < 0) {
                ParGrup2a frame = new ParGrup2a(this, listenerPar2, grup);
            } else {
                List list = ParamList.find(grup).dict();
                ParGrup2b frame = new ParGrup2b(this, listenerPar2, list);
            }
        });
        Util.setSelectedRow(tab1);
    }

    private void selectionTab1(ListSelectionEvent event) {
        Util.clearTable(tab2, tab3, tab4, tab5);
        int row = Util.getSelectedRec(tab1);
        if (row != -1) {
            Record record = qJoining.table(eJoining.up).get(row);
            Integer id = record.getInt(eJoining.id);
            qJoinvar.select(eJoinvar.up, "where", eJoinvar.joining_id, "=", id, "order by", eJoinvar.prio);
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2);
        }
    }

    private void selectionTab2(ListSelectionEvent event) {
        Util.clearTable(tab3, tab4, tab5);
        int row = Util.getSelectedRec(tab2);
        if (row != -1) {
            Record record = qJoinvar.table(eJoinvar.up).get(row);
            Integer id = record.getInt(eJoinvar.id);
            qJoindet.select(eJoindet.up, "where", eJoindet.joinvar_id, "=", id, "order by", eJoindet.artikl_id);
            qJoinpar1.select(eJoinpar1.up, "where", eJoinpar1.joinvar_id, "=", id, "order by", eJoinpar1.grup);
            ((DefaultTableModel) tab3.getModel()).fireTableDataChanged();
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab3);
            Util.setSelectedRow(tab4);
        }
    }

    private void selectionTab4(ListSelectionEvent event) {
        int row = Util.getSelectedRec(tab4);
        if (row != -1) {
            Record record = qJoindet.table(eJoindet.up).get(row);
            Integer id = record.getInt(eJoindet.id);
            qJoinpar2.select(eJoinpar2.up, "where", eJoinpar2.joindet_id, "=", id, "order by", eJoinpar2.grup);
            ((DefaultTableModel) tab5.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab5);
        }
    }

    private void listenerDict() {

        listenerArtikl = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            if (tab1.getBorder() != null) {
                Record joiningRec = qJoining.get(Util.getSelectedRec(tab1));
                if (tab1.getSelectedColumn() == 0) {
                    joiningRec.set(eJoining.artikl_id1, record.getInt(eArtikl.id));
                } else if (tab1.getSelectedColumn() == 1) {
                    joiningRec.set(eJoining.artikl_id2, record.getInt(eArtikl.id));
                }

            } else if (tab4.getBorder() != null) {
                int row = Util.getSelectedRec(tab4);
                Record joindetRec = qJoindet.get(Util.getSelectedRec(tab4));
                joindetRec.set(eJoindet.artikl_id, record.getInt(eArtikl.id));
                joindetRec.set(eJoindet.color_fk, null);
                ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
                Util.setSelectedRow(tab4, row);
            }
        };

        listenerPar1 = (record) -> {
            Util.listenerParam(record, tab3, eJoinpar1.grup, eJoinpar1.numb, eJoinpar1.text, tab1, tab2, tab3, tab4, tab5);
        };

        listenerPar2 = (record) -> {
            Util.listenerParam(record, tab5, eJoinpar2.grup, eJoinpar2.numb, eJoinpar2.text, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColor = (record) -> {
            Util.listenerColor(record, tab4, eJoindet.color_fk, eJoindet.types, tab1, tab2, tab3, tab4, tab5);
        };

        listenerColvar = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab4);
            Record joindetRec = qJoindet.get(Util.getSelectedRec(tab4));
            joindetRec.set(eJoindet.types, record.getInt(0));
            ((DefaultTableModel) tab4.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab4, row);
        };

        listenerJoinvar = (record) -> {
            Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
            int row = Util.getSelectedRec(tab2);
            Record joinvarRec = qJoinvar.get(Util.getSelectedRec(tab2));
            joinvarRec.set(eJoinvar.name, record.getStr(0));
            joinvarRec.set(eJoinvar.types, record.getInt(1));
            if (joinvarRec.get(eJoinvar.prio) == null) {
                int max = 0;
                for (Record rec : qJoinvar) {
                    max = (max < rec.getInt(eJoinvar.prio)) ? rec.getInt(eJoinvar.prio) : max;
                }
                joinvarRec.set(eJoinvar.prio, ++max);
            }
            ((DefaultTableModel) tab2.getModel()).fireTableDataChanged();
            Util.setSelectedRow(tab2, row);
        };
    }

    private void listenerCell() {
        listenerEditor = (component) -> { //слушатель редактирование типа, вида данных и вида ячейки таблицы
            return Util.listenerCell(tab3, tab5, component, tab1, tab2, tab3, tab4, tab5);
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnIns = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        centr = new javax.swing.JPanel();
        pan4 = new javax.swing.JPanel();
        scr1 = new javax.swing.JScrollPane();
        tab1 = new javax.swing.JTable();
        pan1 = new javax.swing.JPanel();
        scr2 = new javax.swing.JScrollPane();
        tab2 = new javax.swing.JTable();
        scr3 = new javax.swing.JScrollPane();
        tab3 = new javax.swing.JTable();
        pan5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        scr4 = new javax.swing.JScrollPane();
        tab4 = new javax.swing.JTable();
        scr5 = new javax.swing.JScrollPane();
        tab5 = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        labFilter = new javax.swing.JLabel();
        txtFilter = new javax.swing.JTextField(){
            public JTable table = null;
        };
        checkFilter = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Соединения");
        setIconImage((new javax.swing.ImageIcon(getClass().getResource("/resource/img32/d033.gif")).getImage()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Joining.this.windowClosed(evt);
            }
        });

        north.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        north.setMaximumSize(new java.awt.Dimension(32767, 31));
        north.setPreferredSize(new java.awt.Dimension(800, 29));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c009.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resource/prop/hint"); // NOI18N
        btnClose.setToolTipText(bundle.getString("Закрыть")); // NOI18N
        btnClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnClose.setFocusable(false);
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setMaximumSize(new java.awt.Dimension(25, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(25, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(25, 25));
        btnClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose(evt);
            }
        });

        btnRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c038.gif"))); // NOI18N
        btnRef.setToolTipText(bundle.getString("Обновить")); // NOI18N
        btnRef.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnRef.setFocusable(false);
        btnRef.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRef.setMaximumSize(new java.awt.Dimension(25, 25));
        btnRef.setMinimumSize(new java.awt.Dimension(25, 25));
        btnRef.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRef.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c034.gif"))); // NOI18N
        btnDel.setToolTipText(bundle.getString("Удалить")); // NOI18N
        btnDel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnDel.setFocusable(false);
        btnDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDel.setMaximumSize(new java.awt.Dimension(25, 25));
        btnDel.setMinimumSize(new java.awt.Dimension(25, 25));
        btnDel.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete(evt);
            }
        });

        btnIns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c033.gif"))); // NOI18N
        btnIns.setToolTipText(bundle.getString("Добавить")); // NOI18N
        btnIns.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnIns.setFocusable(false);
        btnIns.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIns.setMaximumSize(new java.awt.Dimension(25, 25));
        btnIns.setMinimumSize(new java.awt.Dimension(25, 25));
        btnIns.setPreferredSize(new java.awt.Dimension(25, 25));
        btnIns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert(evt);
            }
        });

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c053.gif"))); // NOI18N
        btnReport.setToolTipText(bundle.getString("Печать")); // NOI18N
        btnReport.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        btnReport.setFocusable(false);
        btnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReport.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReport.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReport.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReport(evt);
            }
        });

        javax.swing.GroupLayout northLayout = new javax.swing.GroupLayout(north);
        north.setLayout(northLayout);
        northLayout.setHorizontalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195)
                .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 468, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        northLayout.setVerticalGroup(
            northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northLayout.createSequentialGroup()
                .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(northLayout.createSequentialGroup()
                        .addGroup(northLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        centr.setPreferredSize(new java.awt.Dimension(800, 500));
        centr.setLayout(new java.awt.BorderLayout());

        pan4.setPreferredSize(new java.awt.Dimension(220, 308));
        pan4.setLayout(new java.awt.BorderLayout());

        scr1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr1.setPreferredSize(new java.awt.Dimension(354, 540));

        tab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"zzzzz", "aaaaa", "vvvvvvvvvvvvvvvvv", null, null, null},
                {"ccccc", "vvvvv", "uuuuuuuuuuuuu", null, null, null}
            },
            new String [] {
                "Артикул 1", "Артикул 2", "Название", "Основн...", "Аналог", "ID"
            }
        ));
        tab1.setFillsViewportHeight(true);
        tab1.setName("tab1"); // NOI18N
        tab1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr1.setViewportView(tab1);
        if (tab1.getColumnModel().getColumnCount() > 0) {
            tab1.getColumnModel().getColumn(2).setMinWidth(100);
            tab1.getColumnModel().getColumn(2).setPreferredWidth(140);
            tab1.getColumnModel().getColumn(3).setPreferredWidth(80);
            tab1.getColumnModel().getColumn(3).setMaxWidth(80);
            tab1.getColumnModel().getColumn(5).setMaxWidth(40);
        }

        pan4.add(scr1, java.awt.BorderLayout.CENTER);

        centr.add(pan4, java.awt.BorderLayout.CENTER);

        pan1.setPreferredSize(new java.awt.Dimension(590, 308));
        pan1.setLayout(new javax.swing.BoxLayout(pan1, javax.swing.BoxLayout.LINE_AXIS));

        scr2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr2.setPreferredSize(new java.awt.Dimension(290, 234));

        tab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Мммммммммм", null, null},
                {"2", "Ррррррррррр", null, null}
            },
            new String [] {
                "Приоритет", "Название", "Зеркальность", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab2.setFillsViewportHeight(true);
        tab2.setName("tab2"); // NOI18N
        tab2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr2.setViewportView(tab2);
        if (tab2.getColumnModel().getColumnCount() > 0) {
            tab2.getColumnModel().getColumn(0).setPreferredWidth(52);
            tab2.getColumnModel().getColumn(0).setMaxWidth(80);
            tab2.getColumnModel().getColumn(2).setPreferredWidth(40);
            tab2.getColumnModel().getColumn(2).setMaxWidth(40);
            tab2.getColumnModel().getColumn(3).setMaxWidth(40);
        }

        pan1.add(scr2);

        scr3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr3.setPreferredSize(new java.awt.Dimension(300, 234));

        tab3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"11", "111"},
                {"22", "222"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tab3.setFillsViewportHeight(true);
        tab3.setName("tab3"); // NOI18N
        tab3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr3.setViewportView(tab3);
        if (tab3.getColumnModel().getColumnCount() > 0) {
            tab3.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab3.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        pan1.add(scr3);

        centr.add(pan1, java.awt.BorderLayout.EAST);

        pan5.setPreferredSize(new java.awt.Dimension(800, 200));
        pan5.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(654, 234));
        jPanel3.setLayout(new java.awt.BorderLayout());

        scr4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr4.setPreferredSize(new java.awt.Dimension(454, 234));

        tab4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"yyyyyyyy", "fffffffffffffff", "44", "7", null},
                {"rrrrrrrrrrr", "llllllllllllllllllllllllllll", "77", "2", null}
            },
            new String [] {
                "Артикул", "Название", "Текстура", "Подбор", "ID"
            }
        ));
        tab4.setFillsViewportHeight(true);
        tab4.setName("tab4"); // NOI18N
        tab4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr4.setViewportView(tab4);
        if (tab4.getColumnModel().getColumnCount() > 0) {
            tab4.getColumnModel().getColumn(3).setPreferredWidth(140);
            tab4.getColumnModel().getColumn(3).setMaxWidth(300);
            tab4.getColumnModel().getColumn(4).setMaxWidth(40);
        }

        jPanel3.add(scr4, java.awt.BorderLayout.CENTER);

        scr5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        scr5.setPreferredSize(new java.awt.Dimension(300, 234));

        tab5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"eeeeeeeeee", "22"},
                {"mmmmmmm", "44"}
            },
            new String [] {
                "Параметр", "Значение"
            }
        ));
        tab5.setFillsViewportHeight(true);
        tab5.setName("tab5"); // NOI18N
        tab5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tab5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });
        scr5.setViewportView(tab5);
        if (tab5.getColumnModel().getColumnCount() > 0) {
            tab5.getColumnModel().getColumn(1).setPreferredWidth(80);
            tab5.getColumnModel().getColumn(1).setMaxWidth(120);
        }

        jPanel3.add(scr5, java.awt.BorderLayout.EAST);

        pan5.add(jPanel3, java.awt.BorderLayout.CENTER);

        centr.add(pan5, java.awt.BorderLayout.SOUTH);

        getContentPane().add(centr, java.awt.BorderLayout.CENTER);

        south.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        south.setMinimumSize(new java.awt.Dimension(100, 20));
        south.setPreferredSize(new java.awt.Dimension(800, 20));
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.LINE_AXIS));

        labFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/img24/c054.gif"))); // NOI18N
        labFilter.setText("Поле");
        labFilter.setMaximumSize(new java.awt.Dimension(100, 14));
        labFilter.setMinimumSize(new java.awt.Dimension(100, 14));
        labFilter.setPreferredSize(new java.awt.Dimension(100, 14));
        south.add(labFilter);

        txtFilter.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txtFilter.setMaximumSize(new java.awt.Dimension(180, 20));
        txtFilter.setMinimumSize(new java.awt.Dimension(180, 20));
        txtFilter.setName(""); // NOI18N
        txtFilter.setPreferredSize(new java.awt.Dimension(180, 20));
        txtFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                filterCaretUpdate(evt);
            }
        });
        south.add(txtFilter);

        checkFilter.setText("в конце строки");
        south.add(checkFilter);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose
        this.dispose();
    }//GEN-LAST:event_btnClose

    private void btnRefresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresh
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        loadingData();
        ((DefaultTableModel) tab1.getModel()).fireTableDataChanged();
        Util.setSelectedRow(tab1);
    }//GEN-LAST:event_btnRefresh

    private void btnDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete

        if (tab1.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab2) == 0) {
                Util.deleteRecord(tab1, eJoining.up);
            }
        } else if (tab2.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab3, tab4) == 0) {
                Util.deleteRecord(tab2, eJoinvar.up);
            }
        } else if (tab3.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab3, eJoinpar1.up);
            }
        } else if (tab4.getBorder() != null) {
            if (Util.isDeleteRecord(this, tab5) == 0) {
                Util.deleteRecord(tab4, eJoindet.up);
            }
        } else if (tab5.getBorder() != null) {
            if (Util.isDeleteRecord(this) == 0) {
                Util.deleteRecord(tab5, eJoinpar2.up);
            }
        }
    }//GEN-LAST:event_btnDelete

    private void btnInsert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert

        if (tab1.getBorder() != null) {
            Util.insertRecord(tab1, eJoining.up);

        } else if (tab2.getBorder() != null) {
            Util.insertRecord(tab1, tab2, eJoining.up, eJoinvar.up, eJoinvar.joining_id);

        } else if (tab3.getBorder() != null) {
            Util.insertRecord(tab2, tab3, eJoinvar.up, eJoinpar1.up, eJoinpar1.joinvar_id);

        } else if (tab4.getBorder() != null) {
            Util.insertRecord(tab2, tab4, eJoinvar.up, eJoindet.up, eJoindet.joinvar_id);

        } else if (tab5.getBorder() != null) {
            Util.insertRecord(tab4, tab5, eJoindet.up, eJoinpar2.up, eJoinpar2.joindet_id);
        }
    }//GEN-LAST:event_btnInsert

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
        Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5);
        Arrays.asList(tab1, tab2, tab3, tab4, tab5).forEach(tab -> ((DefTableModel) tab.getModel()).getQuery().execsql());
        if (owner != null) {
            owner.setEnabled(true);
            owner = null;
        }
    }//GEN-LAST:event_windowClosed

    private void btnReport(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReport

    }//GEN-LAST:event_btnReport

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        JTable table = (JTable) evt.getSource();
        Util.listenerClick(table, Arrays.asList(tab1, tab2, tab3, tab4, tab5));
        if (txtFilter.getText().length() == 0) {
            labFilter.setText(table.getColumnName((table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn()));
            txtFilter.setName(table.getName());
        }
    }//GEN-LAST:event_tabMousePressed

    private void filterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_filterCaretUpdate

        JTable table = Stream.of(tab1, tab2, tab3, tab4, tab5).filter(tab -> tab.getName().equals(txtFilter.getName())).findFirst().orElse(tab1);
        btnIns.setEnabled(txtFilter.getText().length() == 0);
        if (txtFilter.getText().length() == 0) {
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(null);
        } else {
            int index = (table.getSelectedColumn() == -1 || table.getSelectedColumn() == 0) ? 0 : table.getSelectedColumn();
            String text = (checkFilter.isSelected()) ? txtFilter.getText() + "$" : "^" + txtFilter.getText();
            ((DefTableModel) table.getModel()).getSorter().setRowFilter(RowFilter.regexFilter(text, index));
        }
    }//GEN-LAST:event_filterCaretUpdate
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnIns;
    private javax.swing.JButton btnRef;
    private javax.swing.JButton btnReport;
    private javax.swing.JPanel centr;
    private javax.swing.JCheckBox checkFilter;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labFilter;
    private javax.swing.JPanel north;
    private javax.swing.JPanel pan1;
    private javax.swing.JPanel pan4;
    private javax.swing.JPanel pan5;
    private javax.swing.JScrollPane scr1;
    private javax.swing.JScrollPane scr2;
    private javax.swing.JScrollPane scr3;
    private javax.swing.JScrollPane scr4;
    private javax.swing.JScrollPane scr5;
    private javax.swing.JPanel south;
    private javax.swing.JTable tab1;
    private javax.swing.JTable tab2;
    private javax.swing.JTable tab3;
    private javax.swing.JTable tab4;
    private javax.swing.JTable tab5;
    private javax.swing.JTextField txtFilter;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    private void initElements() {

        new FrameToFile(this, btnClose);
        labFilter.setText(tab1.getColumnName(0));
        txtFilter.setName(tab1.getName());
        Arrays.asList(btnIns, btnDel, btnRef).forEach(b -> b.addActionListener(l -> Util.stopCellEditing(tab1, tab2, tab3, tab4, tab5)));
        scr1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Списки соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Варианты соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Детализация соединений", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        scr5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0),
                "Параметры", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, common.Util.getFont(0, 0)));
        tab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab1(event);
                }
            }
        });
        tab2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab2(event);
                }
            }
        });
        tab4.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() == false) {
                    selectionTab4(event);
                }
            }
        });
    }
}
