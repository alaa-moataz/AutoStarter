package com.judemanutd.autostarter

/**
 * Created by Alaa Moataz on 19/08/2025.
 */

enum class AutoStartManufacturers(
    val brand: List<String>,
    val intents: List<AutoStartIntent>
) {

    Xiaomi(
        listOf("xiaomi", "poco", "redmi"),
        listOf(
            AutoStartIntent(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            ),
            AutoStartIntent(
                "com.miui.permcenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
        )
    ),

    Letv(
        listOf("letv"),
        listOf(
            AutoStartIntent(
                "com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity"
            )
        )
    ),

    Asus(
        listOf("asus"),
        listOf(
            AutoStartIntent(
                "com.asus.mobilemanager",
                "com.asus.mobilemanager.powersaver.PowerSaverSettings"
            ),
            AutoStartIntent(
                "com.asus.mobilemanager",
                "com.asus.mobilemanager.autostart.AutoStartActivity"
            ),
            AutoStartIntent(
                "com.asus.mobilemanager",
                "com.asus.mobilemanager.autostart.AutoStartServiceActivity"
            )
        )
    ),

    Honor(
        listOf("honor"),
        listOf(
            AutoStartIntent(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.process.ProtectActivity"
            ),
            AutoStartIntent(
                "com.hihonor.systemmanager",
                "com.hihonor.systemmanager.optimize.process.ProtectActivity"
            )
        )
    ),

    Huawei(
        listOf("huawei"),
        listOf(
            AutoStartIntent(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            ),
            AutoStartIntent(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.process.ProtectActivity"
            ),
            AutoStartIntent(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
            )
        )
    ),

    Oppo(
        listOf("oppo"),
        listOf(
            AutoStartIntent(
                "com.coloros.safecenter",
                "com.coloros.safecenter.permission.startup.StartupAppListActivity"
            ),
            AutoStartIntent(
                "com.oppo.safe",
                "com.oppo.safe.permission.startup.StartupAppListActivity"
            ),
            AutoStartIntent(
                "com.coloros.safecenter",
                "com.coloros.safecenter.startupapp.StartupAppListActivity"
            ),
            AutoStartIntent(
                "com.coloros.oppoguardelf",
                "com.coloros.oppoguardelf.ui.startup.StartupAppListActivity"
            )
        )
    ),

    Vivo(
        listOf("vivo"),
        listOf(
            AutoStartIntent(
                "com.iqoo.secure",
                "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
            ),
            AutoStartIntent(
                "com.vivo.permissionmanager",
                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
            ),
            AutoStartIntent(
                "com.iqoo.secure",
                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
            ),
            AutoStartIntent(
                "com.vivo.securedaemonservice",
                "com.vivo.securedaemonservice.activity.BgStartUpManagerActivity"
            )
        )
    ),

    Nokia(
        listOf("nokia"),
        listOf(
            AutoStartIntent(
                "com.evenwell.powersaving.g3",
                "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"
            ),
            AutoStartIntent(
                "com.evenwell.powersaving.g2",
                "com.evenwell.powersaving.g2.exception.PowerSaverExceptionActivity"
            )
        )
    ),

    OnePlus(
        listOf("oneplus"),
        listOf(
            AutoStartIntent(
                "com.oneplus.security",
                "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity"
            ),
            AutoStartIntent(
                "com.oneplus.security",
                "com.oneplus.security.chainlaunch.view.AppAutoLaunchActivity"
            )
        )
    ),

    Realme(
        listOf("realme"),
        listOf(
            AutoStartIntent(
                "com.coloros.safecenter",
                "com.coloros.safecenter.startupapp.StartupAppListActivity"
            ),
            AutoStartIntent(
                "com.realme.securitycenter",
                "com.realme.securitycenter.startupapp.StartupAppListActivity"
            )
        )
    ),

    Meizu(
        listOf("meizu"),
        listOf(
            AutoStartIntent(
                "com.meizu.safe",
                "com.meizu.safe.permission.SmartBGActivity"
            ),
            AutoStartIntent(
                "com.meizu.safe",
                "com.meizu.safe.permission.PermissionMainActivity"
            )
        )
    ),

    Lenovo(
        listOf("lenovo", "motorola"),
        listOf(
            AutoStartIntent(
                "com.lenovo.security",
                "com.lenovo.security.powersetting.PowerAppsWhiteListActivity"
            ),
            AutoStartIntent(
                "com.lenovo.safecenter",
                "com.lenovo.safecenter.autostart.AutoStartActivity"
            )
        )
    );

    companion object{
        val PACKAGE_ONE_PLUS_ACTION = "com.android.settings.action.BACKGROUND_OPTIMIZE"
    }
}