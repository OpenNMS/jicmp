#!/usr/bin/env perl

use File::Spec;
use Getopt::Long qw(:config gnu_getopt);

my $help             = 0;
my $vis_studio       = undef;
my $jdk_home         = undef;

my $result = GetOptions(
        "h|help"              => \$help,
        "j|jdk-home=s"        => \$jdk_home,
        "v|visual-studio=s"   => \$vis_studio,
);

if ($help) {
    usage();
}

if (not defined $jdk_home) {
    usage("-j <jdk-home> is required");
}

if (not defined $vis_studio) {
    usage("-v <visual-studio-ide-dir> is required");
}

$jdk_home = File::Spec->canonpath($jdk_home);

my $contents = "";
print "Updating JAVA_HOME in build files...\n";
open (FILEIN, "win32/jicmp.vcxproj") or die "unable to read from jicmp.vcxproj: $!";
while (my $line = <FILEIN>) {
	$line =~ s,C\:\\Program Files\\Java\\jdk1\.6\.0_30,${jdk_home},g;
	$contents .= $line;
}
close (FILEIN) or die "unable to close jicmp.vcxproj: $!";

open (FILEOUT, '>win32/jicmp.vcxproj') or die "unable to write to jicmp.vcxproj: $!";
print FILEOUT $contents;
close (FILEOUT);

print "Building Java Code\n";
mkdir("classes");
run("$jdk_home\\bin\\javac", "-d", "classes", "-sourcepath", "src/main/java", "src/main/java/org/opennms/protocols/icmp/IcmpSocket.java");

print "Generating JNI Headers\n";
run("$jdk_home\\bin\\javah","-classpath", "classes", "org.opennms.protocols.icmp.IcmpSocket");

print "Building x86 MSM Modules\n";
run("$vis_studio\\devenv", ".\\win32\\jicmp.sln", "-rebuild", "Release|Win32");

print "Building x64 MSM Modules\n";
run("$vis_studio\\devenv", ".\\win32\\jicmp.sln", "-rebuild", "Release|x64");

sub run {
    print(join(" ", @_));print("...");
    handle_errors_and_exit_on_failure(system(@_));
    print("done.\n");
}


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

sub usage {
	my $error = shift;

	print <<END;
usage: $0 [-h] -j <jdk-home> -v visual_studio

	-h            : print this help
	-j            : Home Directory of JDK
	-v            : IDE directory for Visual Studio
END

	if (defined $error) {
		print "ERROR: $error\n\n";
	}

	exit 1;
}

sub error {
    print "[ERROR] " . join(' ', @_) . "\n";
}

sub info {
    print "[INFO] " . join(' ', @_) . "\n";
}
