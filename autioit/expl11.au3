Sleep(1000)
	Local $hIE = WinGetHandle("[Class:IEFrame]")
	Local $hCtrl = ControlGetHandle($hIE, "", "[ClassNN:DirectUIHWND1]")

		WinActivate($hIE,"")
	    ControlSend($hIE ,"",$hCtrl,"{F6}")          ; Gives focus to Open Button
		Sleep(1000)
		ControlSend($hIE ,"",$hCtrl,"{TAB}")          ; Gives focus to Save Button
		ControlSend($hIE ,"",$hCtrl,"{DOWN}")
		ControlSend($hIE ,"",$hCtrl,"{DOWN}")
		Sleep(1000)
		ControlSend($hIE ,"",$hCtrl,"{enter}")        ; Submit whatever control has focus
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
Sleep(4000)


