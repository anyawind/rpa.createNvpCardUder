#include <IE.au3>
_IELoadWait("http://10.3.30.203:9080/ViplataWEB/");
WinWait("Загрузка файла");
WinActivate("Загрузка файла");
ControlFocus("Загрузка файла","","Button2");
ControlClick("Загрузка файла","","[CLASS:Button; INSTANCE:2]","left",1);
ControlClick("Загрузка файла","","[CLASS:Button; INSTANCE:2]","left",1);

WinWait("Сохранить как");
WinActivate("Сохранить как");
ControlFocus("Сохранить как","","Edit1");
Send("{DELETE}");
ControlSetText("Сохранить как","","Edit1",$CmdLine[1]);
ControlClick ("Сохранить как","","Edit1","left",1);
ControlFocus("Сохранить как","","ToolbarWindow323");
ControlClick ("Сохранить как","","ToolbarWindow323","left",1,285,12);
Send("{DELETE}");
Send($CmdLine[2]);
Send("{ENTER}");
ControlFocus("Сохранить как","","Button1");
ControlClick ("Сохранить как" ,"","[CLASS:Button; INSTANCE:1]","left",1);

WinWait("Загрузка завершена.");
WinActivate("Загрузка завершена.");
ControlClick ("Загрузка завершена.","","[CLASS:Button; INSTANCE:4]","left",1);
