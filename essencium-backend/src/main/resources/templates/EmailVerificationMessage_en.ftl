<#import "Header.ftl" as header>
<#import "Footer.ftl" as footer>

<!doctype html>
<html lang="de">
<head>
    <meta name="viewport" content="width=device-width">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${subject}</title>
    <@header.head />
</head>
<body class=""
      style="background-color: #f6f6f6; font-family: sans-serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;">
<span class="preheader"
      style="color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0;">${subject}</span>
<table class="body"
       style="border-collapse: separate; mso-table-lspace: 0; mso-table-rspace: 0; width: 100%; background-color: #f6f6f6;">
    <tr>
        <td style="font-family: sans-serif; font-size: 14px; vertical-align: top;">&nbsp;</td>
        <td class="container"
            style="font-family: sans-serif; font-size: 14px; vertical-align: top; display: block; Margin: 0 auto; max-width: 580px; padding: 10px; width: 580px;">
            <div class="content"
                 style="box-sizing: border-box; display: block; Margin: 0 auto; max-width: 580px; padding: 10px;">

                <!-- START CENTERED WHITE CONTAINER -->
                <table class="main"
                       style="border-collapse: separate; mso-table-lspace: 0; mso-table-rspace: 0; width: 100%; background: #ffffff; border-radius: 3px;">

                    <!-- START MAIN CONTENT AREA -->
                    <tr>
                        <td class="wrapper"
                            style="font-family: sans-serif; font-size: 14px; vertical-align: top; box-sizing: border-box; padding: 20px;">
                            <table style="border-collapse: separate; mso-table-lspace: 0; mso-table-rspace: 0; width: 100%;">
                                <tr>
                                    <td style="font-family: sans-serif; font-size: 14px; vertical-align: top;">
                                        <p style="text-align:center;">
                                            <a href="${mailBranding.url}" style="text-decoration: none;color:black;">
                                                <img src="${mailBranding.logo}" style="max-width: 100px;"
                                                     alt="Application Logo">
                                                <br/><br/>
                                                <span style="font-size: 20px;">${mailBranding.name}</span>
                                            </a>
                                        </p>
                                        <p style="font-family: sans-serif; font-size: 20px; font-weight: bold; margin: 0 0 15px;">
                                            Change of your email address registered</p>
                                        <p style="font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0 0 15px;">
                                            Your email address has just been added to the profile in the application ${mailBranding.name}.</p>
                                        <p style="font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0 0 15px;">
                                            To activate the update, please click on the link below.
                                            After verification, logging in will only be possible with the new email
                                            address.</p>

                                        <table border="0" cellpadding="0" cellspacing="0" class="btn btn-primary"
                                               style="border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; box-sizing: border-box;">
                                            <tbody>
                                            <tr>
                                                <td align="left"
                                                    style="font-family: sans-serif; font-size: 14px; vertical-align: top; padding-bottom: 15px;">
                                                    <table border="0" cellpadding="0" cellspacing="0"
                                                           style="border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: auto;">
                                                        <tbody>
                                                        <tr>
                                                            <td style="font-family: sans-serif; font-size: 14px; vertical-align: top; background-color: ${mailBranding.primaryColor}; border-radius: 5px; text-align: center;">
                                                                <a href="${verifyLink}${verifyToken}" target="_blank"
                                                                   style="display: inline-block; color: ${mailBranding.textColor}; background-color: ${mailBranding.primaryColor}; border: solid 1px ${mailBranding.primaryColor}; border-radius: 5px; box-sizing: border-box; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: bold; margin: 0; padding: 6px 12px; border-color: ${mailBranding.primaryColor};">verify
                                                                    email address</a></td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>

                                        <p style="font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0 0 15px;">
                                            Best regards</p>
                                        <p style="font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0 0 15px;">
                                            <i>Your team from ${mailBranding.name}</i></p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <!-- END MAIN CONTENT AREA -->
                </table>

                <!-- START FOOTER -->
                <@footer.footerDiv/>
                <!-- END FOOTER -->

                <!-- END CENTERED WHITE CONTAINER -->
            </div>
        </td>
        <td style="font-family: sans-serif; font-size: 14px; vertical-align: top;">&nbsp;</td>
    </tr>
</table>
</body>
</html>