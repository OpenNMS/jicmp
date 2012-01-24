// dllmain.cpp : Defines the entry point for the DLL application.
#include "stdafx.h"

BOOL APIENTRY DllMain(HMODULE hMod, DWORD dwReason, LPVOID lpReserved)
{
	switch (dwReason)
	{
	case DLL_PROCESS_ATTACH:
		break;

   case DLL_THREAD_ATTACH:
		break;

   case DLL_THREAD_DETACH:
		break;

   case DLL_PROCESS_DETACH:
		break;
	}

	return TRUE;
}
