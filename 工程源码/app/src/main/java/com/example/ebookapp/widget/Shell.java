package com.example.ebookapp.widget;

/**
 * @author qsj
 * 日期：2021.08.24
 * 书架抽象化
 */
public class Shell {
    private String ShellName;
    private String ShellPath;
    public Shell(String ShellName, String ShellPath){
        this.ShellName = ShellName;
        this.ShellPath = ShellPath;
    }
    public String getShellName(){
        return this.ShellName;
    }
    public String getShellPath(){
        return this.ShellPath;
    }
}
