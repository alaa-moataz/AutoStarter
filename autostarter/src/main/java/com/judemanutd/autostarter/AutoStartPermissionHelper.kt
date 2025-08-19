package com.judemanutd.autostarter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.util.*

class AutoStartPermissionHelper private constructor() {

    /**
     * It will attempt to open the specific manufacturer settings screen with the autostart permission
     * If [open] is changed to false it will just check the screen existence
     *
     * @param context
     * @param open, if true it will attempt to open the activity, otherwise it will just check its existence
     * @param newTask, if true when the activity is attempted to be opened it will add FLAG_ACTIVITY_NEW_TASK to the intent
     * @return true if the activity was opened or is confirmed that it exists (depending on [open]]), false otherwise
     */
    fun getAutoStartPermission(
        context: Context,
        open: Boolean = true,
        newTask: Boolean = false
    ): Boolean {
        val autoStartManufacturer =
            AutoStartManufacturers.entries.firstOrNull {
                it.brand.any { brand -> brand == Build.BRAND.lowercase(Locale.ROOT) }
            }
        if (autoStartManufacturer == null)
            return false
        return when (autoStartManufacturer) {
            AutoStartManufacturers.Oppo -> autoStartOppo(context, open, newTask)

            AutoStartManufacturers.OnePlus -> autoStartOnePlus(context, open, newTask)

            else -> autoStartGeneral(context, open, newTask, autoStartManufacturer)
        }
    }

    /**
     * Checks whether the autostart permission is present in the manufacturer and supported by the library
     *
     * @param context
     * @param onlyIfSupported if true, the method will only return true if the screen is supported by the library.
     *          If false, the method will return true as long as the permission exist even if the screen is not supported
     *          by the library.
     * @return true if autostart permission is present in the manufacturer and supported by the library, false otherwise
     */
    fun isAutoStartPermissionAvailable(
        context: Context,
        onlyIfSupported: Boolean = false
    ): Boolean {
        val pm = context.packageManager
        val autoStartPackages = AutoStartManufacturers.entries
            .flatMap { it.intents.map { intent -> intent.packageName } }

        for (pkg in autoStartPackages) {
            val exists = try {
                pm.getApplicationInfo(pkg, 0)
                true
            } catch (_: PackageManager.NameNotFoundException) {
                false
            }

            if (exists && (!onlyIfSupported || getAutoStartPermission(context, open = false))) {
                return true
            }
        }
        return false
    }

    private fun launchOppoAppInfo(context: Context, open: Boolean, newTask: Boolean): Boolean {
        return try {
            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:${context.packageName}")
            if (open) {
                context.startActivity(i)
                true
            } else {
                isActivityFound(context, i)
            }
        } catch (exx: Exception) {
            exx.printStackTrace()
            false
        }
    }

    @Throws(Exception::class)
    private fun startIntent(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }

    private fun isPackageExists(context: Context, targetPackage: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(targetPackage, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun autoStartGeneral(
        context: Context,
        open: Boolean,
        newTask: Boolean,
        autoStartManufacturers: AutoStartManufacturers
    ): Boolean {
        val mains = autoStartManufacturers.intents
        return autoStart(
            context,
            mains.map { it.packageName },
            mains.map {
                getIntent(it.packageName, it.componentName, newTask)
            },
            open
        )
    }

    private fun autoStartOnePlus(context: Context, open: Boolean, newTask: Boolean): Boolean {
        val mains = AutoStartManufacturers.OnePlus.intents
        return autoStart(
            context,
            mains.map { it.packageName },
            mains.map {
                getIntent(it.packageName, it.componentName, newTask)
            },
            open
        ) || autoStartFromAction(
            context,
            listOf(getIntentFromAction(AutoStartManufacturers.PACKAGE_ONE_PLUS_ACTION, newTask)),
            open
        )
    }

    private fun autoStartOppo(context: Context, open: Boolean, newTask: Boolean): Boolean {
        val mains = AutoStartManufacturers.Oppo.intents
        return if (autoStart(
                context,
                mains.map { it.packageName },
                mains.map {
                    getIntent(it.packageName, it.componentName, newTask)
                },
                open
            )
        ) true
        else launchOppoAppInfo(context, open, newTask)
    }

    /**
     * Generates an intent with the passed package and component name
     * @param packageName
     * @param componentName
     * @param newTask
     *
     * @return the intent generated
     */
    private fun getIntent(packageName: String, componentName: String, newTask: Boolean): Intent {
        return Intent().apply {
            component = ComponentName(packageName, componentName)
            if (newTask) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    /**
     * Generates an intent with the passed action
     * @param intentAction
     * @param newTask
     *
     * @return the intent generated
     */
    private fun getIntentFromAction(intentAction: String, newTask: Boolean): Intent {
        return Intent().apply {
            action = intentAction
            if (newTask) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    /**
     * Will query the passed intent to check whether the Activity really exists
     *
     * @param context
     * @param intent, intent to open an activity
     *
     * @return true if activity is found, false otherwise
     */
    private fun isActivityFound(context: Context, intent: Intent): Boolean {
        return context.packageManager.queryIntentActivities(
            intent, PackageManager.MATCH_DEFAULT_ONLY
        ).isNotEmpty()
    }

    /**
     * Will query the passed list of intents to check whether any of the activities exist
     *
     * @param context
     * @param intents, list of intents to open an activity
     *
     * @return true if activity is found, false otherwise
     */
    private fun areActivitiesFound(context: Context, intents: List<Intent>): Boolean {
        return intents.any { isActivityFound(context, it) }
    }

    /**
     * Will attempt to open the AutoStart settings activity from the passed list of intents in order.
     * The first activity found will be opened.
     *
     * @param context
     * @param intents list of intents
     *
     * @return true if an activity was opened, false otherwise
     */
    private fun openAutoStartScreen(context: Context, intents: List<Intent>): Boolean {
        intents.forEach {
            if (isActivityFound(context, it)) {
                startIntent(context, it)
                return@openAutoStartScreen true
            }
        }
        return false
    }

    /**
     * Will trigger the common autostart permission logic. If [open] is true it will attempt to open the specific
     * manufacturer setting screen, otherwise it will just check for its existence
     *
     * @param context
     * @param packages, list of known packages of the corresponding manufacturer
     * @param intents, list of known intents that open the corresponding manufacturer settings screens
     * @param open, if true it will attempt to open the settings screen, otherwise it just check its existence
     * @return true if the screen was opened or exists, false if it doesn't exist or could not be opened
     */
    private fun autoStart(
        context: Context,
        packages: List<String>,
        intents: List<Intent>,
        open: Boolean
    ): Boolean {
        return if (packages.any { isPackageExists(context, it) }) {
            if (open) openAutoStartScreen(context, intents)
            else areActivitiesFound(context, intents)
        } else false
    }

    /**
     * Will trigger the common autostart permission logic. If [open] is true it will attempt to open the specific
     * manufacturer setting screen, otherwise it will just check for its existence
     *
     * @param context
     * @param intentActions, list of known intent actions that open the corresponding manufacturer settings screens
     * @param open, if true it will attempt to open the settings screen, otherwise it just check its existence
     * @return true if the screen was opened or exists, false if it doesn't exist or could not be opened
     */
    private fun autoStartFromAction(
        context: Context,
        intentActions: List<Intent>,
        open: Boolean
    ): Boolean {
        return if (open) openAutoStartScreen(context, intentActions)
        else areActivitiesFound(context, intentActions)
    }

    companion object {

        private val myInstance by lazy { AutoStartPermissionHelper() }

        fun getInstance(): AutoStartPermissionHelper {
            return myInstance
        }
    }
}
