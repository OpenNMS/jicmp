if [ -x /usr/sbin/semodule ] && [ -x /usr/sbin/selinuxenabled ] && [ -x /usr/sbin/load_policy ] && [ -e /etc/selinux/config ]; then
	. /etc/selinux/config
	if [ "${SELINUXTYPE}" = "targeted" ]; then
		/usr/sbin/semodule -n -s targeted -X 200 -r JICMP 2>/dev/null || :
		/usr/sbin/selinuxenabled && /usr/sbin/load_policy || :
	fi
fi
