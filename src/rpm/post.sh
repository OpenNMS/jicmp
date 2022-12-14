if [ -x "%{_sbindir}/semodule" ]; then
	%{_sbindir}/semodule -n -s targeted -r JICMP 2> /dev/null
	%selinux_modules_install -s targeted %{_datadir}/selinux/packages/JICMP.pp.bz2
fi
