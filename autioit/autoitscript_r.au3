#include <IE.au3>
_IELoadWait("http://10.3.30.203:9080/ViplataWEB/");
WinWait("�������� �����");
WinActivate("�������� �����");
ControlFocus("�������� �����","","Button2");
ControlClick("�������� �����","","[CLASS:Button; INSTANCE:2]","left",1);
ControlClick("�������� �����","","[CLASS:Button; INSTANCE:2]","left",1);

WinWait("��������� ���");
WinActivate("��������� ���");
ControlFocus("��������� ���","","Edit1");
Send("{DELETE}");
ControlSetText("��������� ���","","Edit1",$CmdLine[1]);
ControlClick ("��������� ���","","Edit1","left",1);
ControlFocus("��������� ���","","ToolbarWindow323");
ControlClick ("��������� ���","","ToolbarWindow323","left",1,285,12);
Send("{DELETE}");
Send($CmdLine[2]);
Send("{ENTER}");
ControlFocus("��������� ���","","Button1");
ControlClick ("��������� ���" ,"","[CLASS:Button; INSTANCE:1]","left",1);

WinWait("�������� ���������.");
WinActivate("�������� ���������.");
ControlClick ("�������� ���������.","","[CLASS:Button; INSTANCE:4]","left",1);
