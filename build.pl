#!/usr/bin/env perl

print "ARGS=@ARGV\n";

print "Building Java Code\n";
mkdir("classes");
handle_errors_and_exit_on_failure(system("c:\\Program Files\\Java\\jdk1.6.0_30\\bin\\javac", "-d", "classes", "-sourcepath", "src/main/java", "src/main/java/org/opennms/protocols/icmp/IcmpSocket.java"));

print "Generating JNI Headers\n";
handle_errors_and_exit_on_failure(system("c:\\Program Files\\Java\\jdk1.6.0_30\\bin\\javah","-classpath", "classes", "org.opennms.protocols.icmp.IcmpSocket"));

print "Building x86 MSM Modules\n";
handle_errors_and_exit_on_failure(system("c:\\Program Files\\Microsoft Visual Studio 10.0\\Common7\\IDE\\devenv", ".\\win32\\jicmp.sln", "/rebuild", "Release|Win32"));

print "Building x64 MSM Modules\n";
handle_errors_and_exit_on_failure(system("c:\\Program Files\\Microsoft Visual Studio 10.0\\Common7\\IDE\\devenv", ".\\win32\\jicmp.sln", "/rebuild", "Release|x64"));


sub handle_errors {
    my $exit = shift;
    if ($exit == 0) {
        info("finished successfully");
    } elsif ($exit == -1) {
        error("failed to execute: $!");
    } elsif ($exit & 127) {
    error("child died with signal " . ($exit & 127));
    } else {
    error("child exited with value " . ($exit >> 8));
    }
    return $exit;
}


sub handle_errors_and_exit_on_failure {
    my $exit = handle_errors(@_);
    if ($exit != 0) {
        exit ($exit >> 8);
    }
}


sub error {
    print "[ERROR] " . join(' ', @_) . "\n";
}

sub info {
    print "[INFO] " . join(' ', @_) . "\n";
}
