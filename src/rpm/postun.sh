if [ -x "%{_sbindir}/semodule" ]; then
	%selinux_modules_uninstall -s trusted JICMP
fi
