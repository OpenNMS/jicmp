if [ -x /usr/sbin/semodule ] && [ -x /usr/sbin/selinuxenabled ] && [ -x /usr/sbin/load_policy ] && [ -e /etc/selinux/config ]; then
	. /etc/selinux/config
	/usr/sbin/semodule -n -s targeted -r JICMP 2>/dev/null || :
	if [ "${SELINUXTYPE}" = "targeted" ]; then
		/usr/sbin/semodule -n -s targeted -X 200 -i /usr/share/selinux/packages/JICMP.pp.bz2
		/usr/sbin/selinuxenabled && /usr/sbin/load_policy || :
	fi
fi
